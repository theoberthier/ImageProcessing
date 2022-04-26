import axios, { AxiosResponse } from "axios";
import { Ref } from "vue";

export interface Inter_img{
    name: string,
    id: number,
    type: string,
    size: number,
    undo: boolean,
    redo: boolean,
}
export interface Algo_list{
  id: string,
  name: string,
}

export const api = {
    GetImages(imgs:Ref<Array<Inter_img>>): void{
        axios.get('/images').then(function (response){
         imgs.value = response.data;
      }).then(function(response){
        console.log("liste chargé");
      }).catch(function(response){
        console.log("liste non chargé");       
      });
    },
    GetImage(balise:Ref<string>, id:number): void{
           var imgUrl = "/images/"+id;
           axios.get(imgUrl, {
           responseType:"blob"
           }).then(function (response: AxiosResponse){
           const reader = new window.FileReader();
             reader.readAsDataURL(response.data);
             reader.onload = function() {
                 const imageDataUrl = (reader.result as string);
                 balise.value = imageDataUrl
             }
           }).catch(function (){
             console.log("image non get");
           })
    },
    GetImage(balise:Ref<string>, id:number, algo:String, p1:String | null, v1:Number | null, p2:String | null,v2:Number | null): void{
      if(p2 === null && v2 === null)
        var imgUrl = "/images/"+id+"?algorithm="+algo+"&"+p1+"="+v1;
      else
        var imgUrl = "/images/"+id+"?algorithm="+algo+"&"+p1+"="+v1+"&"+p2+"="+v2;
      axios.get(imgUrl, {
      responseType:"blob"
      }).then(function (response: AxiosResponse){
      const reader = new window.FileReader();
        reader.readAsDataURL(response.data);
        reader.onload = function() {
            const imageDataUrl = (reader.result as string);
            balise.value = imageDataUrl
        }
      })
    },
    PostImage(file:File,callback?:Function):void{
    let formData = new FormData();
    formData.append('file', file);
     axios.post( '/images',
      formData,
      {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
      }
    ).then(function(){
      if(callback){
        callback();
      }
      console.log('SUCCESS!!');
      alert("photo envoyé !");
    })
    .catch(function(){
      console.log('FAILURE!!');
      alert("photo non envoyé !")
    });
    },
    Delete(id:Number,callback:Function):void{
      axios.delete("images/"+id).then(function(){
        if(callback){
          callback()
        }
        alert("photo supprimé !");
      })
      .catch(function(){
        alert("photo non supprimé !")
      });
    },
    Undo(id:Number,callback?:Function):void{
      axios.get("undo/"+id).then(function (){
        if(callback){
          callback();
        }
        console.log("undo effecctué sur id : "+id);
      }).catch(function (){
        console.log("undo non effectué sur id : "+id);
      });
    },
    Redo(id:Number,callback?:Function):void{
      axios.get("redo/"+id).then(function (){
        if(callback){
          callback();
        }
        console.log("Redo effecctué sur id : "+id);
      }).catch(function (){
        console.log("Redo non effectué sur id : "+id);
      });
    },
    Replace(id:Number,callback?:Function):void{
      axios.get("replace/"+id).then(function (){
        if(callback){
          callback();
        }
        console.log("Redo effecctué sur id : "+id);
      }).catch(function (){
        console.log("Redo non effectué sur id : "+id);
      });
    },
    Reset(id:Number):void{
      axios.get("reset/"+id).then(function (){
        console.log("reset effecctué sur id : "+id);
      }).catch(function (){
        console.log("reset non effectué sur id : "+id);
      });
    },
    GetAlgos(list:Ref<Array<Algo_list>>): void{
      axios.get('/algos').then(function (response){
       list.value = response.data;
    }).then(function(response){
      console.log("liste d'algo chargé");
    }).catch(function(response){
      console.log("liste d'algo non chargé");       
    });
    },
}

