
<script setup lang="ts">
import { api } from "../http-api";
const props =  defineProps({
  updateImgs:{
    type: Function,
    default: () => {}
  },
})

  let file:File;

  function submitFile(){
     api.PostImage(file,props.updateImgs);
  }


  function handleFileUpload( event:Event ): void{
      let target = event.target;
      if(target){
         let files =  (<HTMLInputElement> target).files;
         if(files){
           file = files[0]
         }
      }
        console.log(file)
  }

</script>

<template>
    
    <div class="addFile">
      <label>File
        <input id="btn-browser" type="file" @change="handleFileUpload($event)"/>
      </label>
      <button v-on:click="submitFile">Submit</button>   
    </div>

</template>

<style scoped>

</style>