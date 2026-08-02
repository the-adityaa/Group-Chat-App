const colors = [
  "bg-violet-500",
  "bg-blue-500",
  "bg-green-500",
  "bg-orange-500",
  "bg-pink-500",
  "bg-cyan-500",
  "bg-red-500",
  "bg-indigo-500",
  "bg-emerald-500",
  "bg-amber-500",
];

export const getUserColor = (username = "") => {
  let hash = 0;

  for (let i = 0; i < username.length; i++) {
    hash = username.charCodeAt(i) + ((hash << 5) - hash);
  }

  return colors[Math.abs(hash) % colors.length];
};