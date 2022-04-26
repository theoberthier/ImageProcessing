
<script setup lang="ts">
import { api,Inter_img } from "../http-api";
const props =  defineProps({
  updateImgs:{
    type: Function,
    default: () => {}
  },
  updateId:{
    type:Function,
    default: (arg:Number) => {},
  },
  id_selected:Number,
})


function delImage(){
  api.Delete(props.id_selected,props.updateImgs);
}

</script>

<template>
    <div class="del-container" @click="delImage()">
      <!-- bouton del ne fonctionne pas, voir si faut enlever bouton et mettre un @onclik sur la div et appeler api.delete() ? -->
      <div class="del"></div>
    </div>
</template>

<style scoped>
/**********btn-delete *********/
.del-container{
  width: 5%;
  height: 8%;
  position: fixed;
  right: 1%;
  top: 3%;
  opacity: 0.1;

}
.del-container:hover{
  opacity: 1;
  transition: 0.5s;
  background-color: rgba(200,200,200,.7);
}
.del-container{
  transition: 0.5s;
  border-radius: 5px;
}
.del{
  width: 80%;
  height: 10%;
  background-color: red;
  border-style: none;
  position: relative;
  transform: translateY(1.5em);
  border-radius: 20px;
  margin: auto;
}

</style>
