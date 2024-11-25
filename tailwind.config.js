/** @type {import('tailwindcss').Config} */
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

module.exports = {
  plugins: [
    require('flowbite/plugin')
  ],
  content: [
    "./node_modules/flowbite/**/*.js","./src/main/resources/**/*.{html,js}",
  ]
}


