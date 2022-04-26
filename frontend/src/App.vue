<script setup lang="ts">
// This starter template is using Vue 3 <script setup> SFCs
// Check out https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup


import ImageProcessing from './components/imageProcessing.vue'
import Post from './components/post.vue'
import Delete from './components/delete.vue'
import { api } from './http-api';
import { ref, Ref } from 'vue';
import { Inter_img } from './http-api';
import { looseIndexOf } from '@vue/shared';
let tab_img:Ref<Array<Inter_img>> = ref([]);
let img_id_select:Ref = ref(1);
function updateImage(){
  api.GetImages(tab_img);
  console.log("inside upade image");
  
}

function onChangeImage(arg:Number){
  img_id_select.value = arg;
}


updateImage();


</script>

<template>
  <!--<img alt="Vue logo" src="./assets/logo.png" />-->
  <ImageProcessing :updateImgs="updateImage" :id_selected="img_id_select" :images="tab_img" :updateId="onChangeImage" class="imageProcess"/>
  <Post id="post" :updateImgs="updateImage" class="post"/>
  <Delete :id_selected="img_id_select" :updateImgs="updateImage" class="delete" />
 
</template>

<style>
body{
  margin: 0;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
#post{ 
  position: fixed;
  bottom: 5.5%;
  left: 43%;
 }

</style>
