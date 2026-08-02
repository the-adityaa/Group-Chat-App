import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import toast from 'react-hot-toast'
import JoinCreateChat from "./components/JoinCreateChat"

function App() {
  const [count, setCount] = useState(0)

  return (
    <div>
        <JoinCreateChat />
    </div>
  )

}

export default App
