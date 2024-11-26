export default {
  content: ["./src/main/resources/**/*.{html,js}"
  ],
  theme: {
    extend: {},
  },
  plugins: [
  ],
  darkMode: "selector",
}
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}","./node_modules/flowbite/**/*.js"],
  theme: {
    extend: {},
  },
  plugins: [require('flowbite/plugin')],
  darkMode: "selector",
}


