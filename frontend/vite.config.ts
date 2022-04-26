import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '^/images': {
        target: 'http://localhost:8080' // Spring boot backend address
      },
      '^/undo':{
        target: 'http://localhost:8080' // Spring boot backend undo
      },
      '^/redo':{
        target: 'http://localhost:8080' // Spring boot backend redo
      },
      '^/replace':{
        target: 'http://localhost:8080' // Spring boot backend replace
      },
      '^/reset':{
        target: 'http://localhost:8080' // Spring boot backend reset
      },
      '^/algos':{
        target: 'http://localhost:8080' // Spring boot backend list algos
      },
    }
  }
})
