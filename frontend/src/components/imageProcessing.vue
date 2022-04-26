<script setup lang="ts">
import { ref,Ref,toRef } from 'vue'
import { api, Inter_img, Algo_list } from '../http-api';

const props =  defineProps({
  images:{ 
    type:Array as () => Array<Inter_img>,
    required:true
  },
  updateImgs:{
    type:Function,
    default: () => {},
  },
  updateId:{
    type:Function,
    default: (arg:Number) => {},
  },
  id_selected:Number,
  });

let id_select = ref(props.images[0].id);
let indiceImage = ref(0);
let img_src_select = ref("");
let algo_list:Ref<Array<Algo_list>> = ref([]);


let algoParam:{
  lum_delta:  Ref<number>,
  blur_Mean: Ref<boolean>,
  blur_Goss: Ref<boolean>,
  blur_delta:  Ref<number>,
  color_delta:  Ref<number>,
  ridge: Ref<boolean>,
  histo_choiceS: Ref<boolean>,
  histo_choiceV: Ref<boolean>,
  algo_simple: Ref<string>,
} = {
    lum_delta: ref(0),
    blur_Mean: ref(false),
    blur_Goss: ref(false),
    blur_delta: ref(0),
    color_delta: ref(0),
    ridge: ref(false),
    histo_choiceS: ref(false),
    histo_choiceV: ref(false),
    algo_simple: ref(""),
}
function prev(){
  if(indiceImage.value > 0)
   indiceImage.value -= 1;
  else
    indiceImage.value = props.images.length - 1;
  id_select.value = props.images[indiceImage.value].id;
  changeImg(id_select.value);
  props.updateId(id_select.value);
 }
function next(){
  if(indiceImage.value+1 > props.images.length-1)
    indiceImage.value = 0;
  else
    indiceImage.value += 1;
  id_select.value = props.images[indiceImage.value].id;
  changeImg(id_select.value);
  props.updateId(id_select.value);
}

function changeImg(arg:number){
  api.GetImage(img_src_select,arg);
  props.updateId(arg);
  id_select.value = arg;
}

changeImg(props.id_selected);

function listEvent(arg:number){
  changeImg(arg);
  indiceImage.value = props.images.findIndex((element)=> element.id == arg);
}



function applyAlgo(){
  api.Reset(id_select.value);
  console.log(algoParam.algo_simple);
  
  if(algoParam.histo_choiceS.value === true && algoParam.histo_choiceV.value === true){
    document.getElementById("hist").style.backgroundColor = "red";
    return false;
  }
  else{
   document.getElementById("hist").style.backgroundColor = "white";
  }
  if(algoParam.blur_Mean.value === true && algoParam.blur_Goss.value === true){
    document.getElementById("blur").style.backgroundColor = "red";
    return false;
  }
  else{ 
    document.getElementById("blur").style.backgroundColor = "white";
  }
  if(algoParam.lum_delta.value != 0){
    api.GetImage(img_src_select,id_select.value,"luminosity","delta",algoParam.lum_delta.value,null,null);
  }
  if(algoParam.blur_Mean.value === true && algoParam.blur_delta.value > 0)
    api.GetImage(img_src_select,id_select.value,"blur","radius",algoParam.blur_delta.value,"filter",1);
  if(algoParam.blur_Goss.value === true && algoParam.blur_delta.value > 0)
    api.GetImage(img_src_select,id_select.value,"blur","radius",algoParam.blur_delta.value,"filter",2);
  if(algoParam.color_delta.value != 0)
    api.GetImage(img_src_select,id_select.value,"color","delta",algoParam.color_delta.value,null,null);
  if(algoParam.histo_choiceS.value === true)
    api.GetImage(img_src_select,id_select.value,"histo","band",1,null,null);
  if(algoParam.histo_choiceV.value === true)
    api.GetImage(img_src_select,id_select.value,"histo","band",2,null,null);
  if(algoParam.ridge.value === true)
    api.GetImage(img_src_select,id_select.value,"ridge",null,null,null,null);
  if(algoParam.algo_simple.value != "none")
    api.GetImage(img_src_select,id_select.value,algoParam.algo_simple,null,null,null,null);
  else
    api.GetImage(img_src_select,id_select.value);

}


function undo(){
  api.Undo(id_select.value,props.updateImgs);
  changeImg(id_select.value);
}

function redo(){
  api.Redo(id_select.value,props.updateImgs);
  changeImg(id_select.value);
}

function replace(){
  api.Replace(id_select.value,props.updateImgs);
}

function download(){
  document.getElementById("down").setAttribute("href",img_src_select.value);
}
api.GetAlgos(algo_list);

// voir watch avec vue ts pour observer la variable id_select pour appeler changeImage() pour changer l'affichage de l'image supprimé
</script>

<template>
  <div class="list-container">
    <h3>liste d'image</h3>
    <ul class="list-img">
        <li v-for="item in props.images" :key="item.id"><button @click="listEvent(item.id)">{{ item.id }} : {{item.name}}</button></li>
    </ul>
  </div>
  <div class="Slider">
  <button @click="prev" type="button" class="btn-slide btn-left"><img src="../assets/chevron-gauche.png" alt=""></button>
  <div class="slider-container" >
    <div class="metadata">
      <ul>
        <li class="item-metadata" v-for="(value, name) in props.images[props.images.findIndex((element) => element.id == props.id_selected)]">{{ name }} : {{ value }}</li>
      </ul>
    </div>
    <img class="slide-img" :src="img_src_select" alt="" />
  </div>
  
  <button @click="next" type="button" class="btn-slide btn-right"><img src="../assets/chevron-droite.png" alt=""></button>
  </div>

  <div class="effect-container">
    <ul class="effect-list">
        <label for="">Luminance {{ algoParam.lum_delta.value }}
        <li><input type="range" min="-100" max="100" v-model="algoParam.lum_delta.value"/></li></label>
        <label id="blur" for="blur">Blur Filter {{ algoParam.blur_delta.value }}
        <li><input type="range" min="0" max="10" v-model="algoParam.blur_delta.value"></li>
        <li><label for="case1">Mean<input type="checkbox" v-model="algoParam.blur_Mean.value"></label></li>
        <li><label for="case2">Gossian<input type="checkbox" v-model="algoParam.blur_Goss.value"></label></li> 
        </label>
        
        <label for="">Color Filter {{ algoParam.color_delta.value }}
        <li><input type="range" min="-60" max="100" v-model="algoParam.color_delta.value"></li></label>
        <label id="hist" for="">histogram equalizer 
        <li><label for="case1">S<input  v-model="algoParam.histo_choiceS.value" type="checkbox"></label></li>
        <li><label for="case2">V<input v-model="algoParam.histo_choiceV.value" type="checkbox"></label></li> 
        </label>
        <label for="">Ridge Filter<li><input type="checkbox" v-model="algoParam.ridge.value"></li></label>
        <label for="">another algorithm<li><select v-model="algoParam.algo_simple">
          <option>none</option>
          <option  v-for="algo in algo_list" :value="algo.id" :key="algo.id">{{ algo.name }}</option>
        </select></li></label>
        <li><button id="apply" @click="applyAlgo">apply</button></li>
        
    </ul>
    <ul class="nav">
      <li v-if="props.images[indiceImage].undo == true"><button @click="undo" class="btn-undo">undo</button></li>
      <li v-if="props.images[indiceImage].redo == true"><button @click="redo" class="btn-redo">redo</button></li>
      <li><a href="" id="down" class="btn-redo" @click="download" download="img.jpg"><button> download</button></a></li>
      <li class="btn-redo btn-replace" @click="replace"><button>replace image</button></li>
    </ul>
</div>

</template>

<style scoped>
  /***********liste img css************/
    body{
      width: 100%;
      height: 100%;
      margin: 0;
    }
    h3{
      margin: 10%;
    }
    .list-container{
        width: 19.55%;
        height: 100%;
        border: solid black;
        margin: 0;
        position: fixed;
        top: 0;

    }
    .list-img{
      width: 100%;
      position: relative;
      left: -15%;
    }
    .list-img li{
      list-style: none;
      width: 100%;
      margin: auto;
      left: 0;
    }
    .list-img li:hover, .list-img li button:hover{
      background-color: grey;
    }
    .list-img button{
      background:none;
      background-color: white;
      border: none;
      width: 100%;
    }
    /*********carousel css*********/
  .Slider{
    width: 80%;
    height: 70%;
    margin: auto;
    overflow: hidden;
    position: fixed;
    top:0;
    right:0;
  }
  .slider-container{
    display: flex;
    height: 100%;
    width: 100%;
  }
  .slide-img{
    width: 100%;
    height: 100%;
    position: absolute;
    z-index: -5;
  }
  .btn-slide{
    width: 8%;
    height: 30%;
    text-align: center;
    border: none;
    background: none;
    position: absolute;
    z-index: 1000;
    display: block;
    transition: 0.5s;
  }
  .btn-slide:hover{
    background-color: rgba(200,200,200,.7);
  }
  .btn-left,.btn-right{
    top: 50%;
    transform: translateY(-50%);
  }
  .btn-left{
    left:0%;
  }
  .btn-right{
    right: 0%;
  }
  button img{
    width: 70%;
    height: 35%;
    margin: auto;
  }

/*********** case effect css *********/
  .effect-container{
    width: 80%;
    border: solid black;
    position: fixed;
    top: 70%;
    left: 20%;
  }
  .effect-container ul{
    display: flex;
    list-style-type: none;
    width: 80%;
  }
  label{
    margin-right:5%;
  }
  #apply{
    width: 200%;
  }
  #btn-browser{
    margin: 1%;
    left: 0%;
  }
  .btn-undo,.btn-redo{
    width: 100%;
    height: 100%;
  }
  .nav li{
    padding: 1%;
  }
  .btn-replace{
    width: 11%;
    padding: 0;
  }
/********* metadata style ************/
.metadata {
  height: 20%;
  width: 20%;
  text-align: left;
  position:fixed;
  z-index: 500000;
  opacity: 0;
  transition: 0.5s;
}

.metadata li{
  list-style-type: none;
}
.metadata:hover{
  background-color: rgba(200,200,200,.5);
  opacity: 1;
  transition: 0.5s;
}
</style>