import React, { useState, useRef, useEffect } from 'react'
import { MdAttachFile, MdAttachment, MdOutlineAttachMoney, MdSend } from "react-icons/md";
import useChatContext from '../context/ChatContext'
import { useNavigate } from 'react-router';
import SockJS from 'sockjs-client';
import { baseURL } from '../config/AxiosHelper';
import { Stomp } from '@stomp/stompjs';

import { timeAgo } from "../config/helper";
import { getMessagess } from "../services/RoomService";
import toast from "react-hot-toast";

import { getUserColor } from "../utils/chatColor";
import EmojiPicker from "emoji-picker-react";
import { BsEmojiSmile } from "react-icons/bs";

import axios from "axios";





const ChatPage = () => {

  const {
    roomId,
    currentUser,
    connected,
    setConnected,
    setRoomId,
    setCurrentUser,
  } = useChatContext();
  console.log(roomId);
  console.log(currentUser);
  console.log(connected);

  const navigate = useNavigate()
  useEffect(() => {
    if (!connected) {
      navigate("/")
    }
  }, [connected, roomId, currentUser])

  const [messages, setMessages] = useState([]);
  const [showEmojiPicker, setShowEmojiPicker] = useState(false);
  const [input, setInput] = useState("");
  const inputRef = useRef(null);
  const chatBoxRef = useRef(null);
  const [stompClient, setStompClient] = useState(null);
  const [onlineUsers, setOnlineUsers] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);


  // page init:
  // loading messages

  useEffect(() => {
    async function loadMessages() {
      try {
        const messages = await getMessagess(roomId);
        // console.log(messages);
        setMessages(messages);
      } catch (error) { }
    }
    if (connected) {
      loadMessages();
    }
  }, [connected, roomId]);

  //scroll down

  useEffect(() => {
    if (chatBoxRef.current) {
      chatBoxRef.current.scroll({
        top: chatBoxRef.current.scrollHeight,
        behavior: "smooth",
      });
    }
  }, [messages]);


  // stompClient ko init -- subscribe

  useEffect(() => {
    const connectWebSocket = () => {
      const sock = new SockJS(`${baseURL}/chat`)
      const client = Stomp.over(sock)
      client.connect({}, () => {

        setStompClient(client);

        toast.success("Connected");

        // Chat Messages
        client.subscribe(`/topic/room/${roomId}`, (message) => {

          const newMessage = JSON.parse(message.body);

          setMessages((prev) => [...prev, newMessage]);

        });

        // Online Users
        client.subscribe(`/topic/room/${roomId}/users`, (message) => {

          const response = JSON.parse(message.body);

          setOnlineUsers(response.users);

        });

        // Join Room
        client.send(
          "/app/join",
          {},
          JSON.stringify({
            roomId: roomId,
            username: currentUser,
          })
        );

      });
    };

    if (connected) {
      connectWebSocket();
    }
    //stomp client
  }, [roomId]);

  // send msg handle

  const onEmojiClick = (emojiData) => {
    setInput((prev) => prev + emojiData.emoji);
    inputRef.current?.focus();
  };

  const uploadFile = async () => {

    console.log(selectedFile);
    console.log(selectedFile.type);
    console.log(selectedFile.name);

    alert(selectedFile.type);

    if (!selectedFile) return null;

    const formData = new FormData();
    formData.append("file", selectedFile);

    try {

      const response = await axios.post(
        `${baseURL}/api/files/upload`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      return response.data;

    } catch (error) {

      console.error(error);

      console.log("Status:", error.response?.status);
      console.log("Data:", error.response?.data);
      console.log("Message:", error.message);

      alert(
        JSON.stringify({
          status: error.response?.status,
          data: error.response?.data,
          message: error.message,
        })
      );

      toast.error("File upload failed");

      return null;
    }
  };

  const sendMessage = async () => {

    console.log("Send Clicked");
    console.log(selectedFile);

    if (!stompClient || !connected) return;

    let uploadResponse = null;

    if (selectedFile) {
      uploadResponse = await uploadFile();

      if (!uploadResponse) return;
    }

    if (!input.trim() && !uploadResponse) {
      return;
    }

    const message = {
      sender: currentUser,
      roomId: roomId,

      content: input,

      fileUrl: uploadResponse?.fileUrl || null,
      fileName: uploadResponse?.fileName || null,
      messageType: uploadResponse?.messageType || "TEXT",
    };

    stompClient.send(
      `/app/sendMessage/${roomId}`,
      {},
      JSON.stringify(message)
    );

    setInput("");
    setSelectedFile(null);

    document.getElementById("fileInput").value = "";
  };

  function handleLogout() {

    if (stompClient && stompClient.connected) {

      // Leave Room
      stompClient.send(
        "/app/leave",
        {},
        JSON.stringify({
          roomId: roomId,
          username: currentUser,
        })
      );

      stompClient.disconnect();
    }

    setConnected(false);
    setRoomId("");
    setCurrentUser("");

    navigate("/");
  }


  return (
    <div className="h-screen flex flex-col bg-gray-800">
      {/* This is a Header Portion*/}
      <header className="fixed top-0 left-0 z-10 w-full py-5 shadow dark:bg-gray-900 flex justify-around items-center">
        {/* room name container*/}
        <div className="">
          <h1 className="text-xl font-semibold">
            Room : <span>{roomId}</span></h1>
        </div>
        {/* username container*/}
        {/* Online Users */}
        <div className="flex flex-wrap items-center gap-2">

          {onlineUsers.map((user) => {

            const color = getUserColor(user);

            return (

              <div
                key={user}
                className={`flex items-center gap-2 rounded-full px-3 py-1 ${user === currentUser ? color : "bg-slate-800"
                  }`}
              >

                <span
                  className={`h-2 w-2 rounded-full ${user === currentUser ? "bg-white" : "bg-green-500"
                    }`}
                />

                <span
                  className={`text-sm font-semibold ${user === currentUser ? "text-white" : "text-white"
                    }`}
                >
                  {user === currentUser
                    ? `${user} (YOU)`
                    : user}
                </span>

              </div>
            );

          })}

        </div>
        {/* button : leave room*/}
        <div>
          <button onClick={handleLogout}
            className="dark:bg-red-500 dark:hover:bg-red-700 px-3 py-2 rounded-full">Leave Room</button>
        </div>
      </header>

      {/* Chat Content */}
      <main
        ref={chatBoxRef}
        className="flex-1 overflow-y-auto bg-slate-700 px-6 py-24 pb-32"
      >
        <div className="mx-auto max-w-5xl">

          {messages.map((message, index) => {

            const bubbleColor = getUserColor(message.sender);
            message.sender === currentUser
              ? "bg-purple-700"
              : getUserColor(message.sender);

            return (
              <div
                key={index}
                className={`mb-4 flex animate-message ${message.sender === currentUser
                  ? "justify-end"
                  : "justify-start"
                  }`}
              >
                <div
                  className={`max-w-md rounded-2xl shadow-lg transition duration-200 hover:scale-[1.02] ${bubbleColor}`}
                >
                  <div className="flex gap-3 p-4">

                    <img
                      className="h-11 w-11 rounded-full border-2 border-white/20 bg-white"
                      src={`https://api.dicebear.com/9.x/adventurer/svg?seed=${message.sender}`}
                      alt={message.sender}
                    />

                    <div>

                      <p className="text-sm font-semibold text-white">
                        {message.sender}
                      </p>

                      {message.messageType === "IMAGE" ? (

                        <img
                          src={`${baseURL}${message.fileUrl}`}
                          alt="chat"
                          className="mt-2 rounded-lg max-w-[250px] cursor-pointer"
                        />

                      ) : message.messageType === "PDF" ? (

                        <a
                          href={`${baseURL}${message.fileUrl}`}
                          target="_blank"
                          rel="noreferrer"
                          className="text-blue-200 underline"
                        >
                          📄 {message.fileName}
                        </a>

                      ) : (

                        <p className="mt-1 whitespace-pre-wrap break-words text-[15px] text-white">
                          {message.content}
                        </p>

                      )}

                      <p className="mt-2 text-[11px] text-gray-200">
                        {timeAgo(message.timeStamp)}
                      </p>

                    </div>

                  </div>
                </div>
              </div>
            );
          })}

        </div>
      </main>


      {/* Input Message container*/}
      <div className="fixed bottom-4 left-0 w-full px-4">
        <div className="relative mx-auto flex h-16 w-full max-w-4xl items-center gap-3">

          {selectedFile && (
            <div className="absolute bottom-20 left-20 flex items-center gap-3 rounded-lg bg-gray-800 px-3 py-2 shadow-lg">

              {selectedFile.type.startsWith("image") ? (
                <img
                  src={URL.createObjectURL(selectedFile)}
                  alt="preview"
                  className="h-16 w-16 rounded object-cover"
                />
              ) : (
                <div className="text-white">
                  📄 {selectedFile.name}
                </div>
              )}

              <button
                onClick={() => {
                  setSelectedFile(null);
                  document.getElementById("fileInput").value = "";
                }}
                className="rounded bg-red-600 px-2 py-1 text-white hover:bg-red-700"
              >
                ✕
              </button>

            </div>
          )}

          {/* Emoji Picker */}
          {showEmojiPicker && (
            <div className="absolute bottom-20 left-0 z-50">
              <EmojiPicker
                onEmojiClick={onEmojiClick}
                theme="dark"
                width={320}
                height={400}
              />
            </div>
          )}

          {/* Emoji Button */}
          <button
            onClick={() => setShowEmojiPicker(!showEmojiPicker)}
            className="flex h-14 w-14 items-center justify-center rounded-full bg-yellow-500 hover:bg-yellow-600 transition"
          >
            <BsEmojiSmile size={22} />
          </button>

          {/* Message Input */}
          <input
            ref={inputRef}
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                sendMessage();
              }
            }}
            type="text"
            placeholder="Type your message..."
            className="flex-1 h-full rounded-full border border-gray-600 bg-gray-800 px-6 text-white placeholder-gray-400 outline-none focus:ring-2 focus:ring-blue-500"
          />

          {/* Attach */}
          <input
            id="fileInput"
            type="file"
            accept="image/*,.pdf"
            style={{ display: "none" }}
            onChange={(e) => {
              if (e.target.files.length > 0) {
                setSelectedFile(e.target.files[0]);
              }
            }}
          />

          <button
            onClick={() => document.getElementById("fileInput").click()}
            className="flex h-14 w-14 items-center justify-center rounded-full bg-blue-600 hover:bg-blue-700 transition"
          >
            <MdAttachFile size={24} />
          </button>

          {/* Send */}
          <button
            onClick={sendMessage}
            className="flex h-14 w-14 items-center justify-center rounded-full bg-green-600 hover:bg-green-700 transition"
          >
            <MdSend size={24} />
          </button>

        </div>
      </div>

    </div>
  )
}
export default ChatPage

