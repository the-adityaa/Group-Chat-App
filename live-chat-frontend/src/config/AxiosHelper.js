import axios from 'axios';

// export const baseURL = "http://localhost:8080";

// export const baseURL = "http://192.168.0.102:8080";

export const baseURL =
  "https://fish-clerk-invest-certificate.trycloudflare.com";


export const httpClient = axios.create({
  baseURL,
})
