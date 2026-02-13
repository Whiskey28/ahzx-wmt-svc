import{_ as A}from"./Dialog.vue_vue_type_style_index_0_lang-DdBxR7Ve.js";import{a as E,c as H,aF as L,R as M}from"./index-DT4sM4a5.js";import{_ as T}from"./ContentWrap.vue_vue_type_script_setup_true_lang-BBfMuJB9.js";import{u as $}from"./useFormCreateDesigner-Dnjow_th.js";import{H as g}from"./index-Bb9BNn-J.js";import{j as q}from"./java-B6oG8O6W.js";import{j as K}from"./json-D1vBrYzr.js";import{l as W,r as l,e as P,ac as U,ag as G,m as w,q as _,K as o,D as n,t as m,u as s,H as c,F as Q,I as X,E as Y,f as Z,J as ee,al as ae}from"./form-create-D_toYIVM.js";import{f as oe,e as ne}from"./form-designer-DBKh_XjC.js";import"./index-BCN8BzfC.js";import"./dict.type-2QGJ0QUa.js";const se={class:"h-[calc(100vh-var(--top-tool-height)-var(--tags-view-height)-var(--app-content-padding)-var(--app-content-padding)-2px)]"},te={key:0,ref:"editor"},le={class:"hljs"},ie=W({name:"InfraBuild",__name:"index",setup(re){const{t:d}=E(),h=H(),C=l({switchType:[],autoActive:!0,useTemplate:!1,formOptions:{form:{labelWidth:"100px"}},fieldReadonly:!1,hiddenDragMenu:!1,hiddenDragBtn:!1,hiddenMenu:[],hiddenItem:[],hiddenItemConfig:{},disabledItemConfig:{},showSaveBtn:!1,showConfig:!0,showBaseForm:!0,showControl:!0,showPropsForm:!0,showEventForm:!0,showValidateForm:!0,showFormConfig:!0,showInputData:!0,showDevice:!0,appendConfigData:[]}),t=l(),i=l(!1),v=l(""),u=l(-1),r=l("");$(t);const f=a=>{i.value=!0,v.value=a},O=()=>{f("\u751F\u6210 JSON"),u.value=0,r.value=t.value.getRule()},S=()=>{f("\u751F\u6210 Options"),u.value=1,r.value=t.value.getOption()},J=()=>{f("\u751F\u6210\u7EC4\u4EF6"),u.value=2,r.value=b()},b=()=>{const a=t.value.getRule(),e=t.value.getOption();return`<template>
    <form-create
      v-model:api="fApi"
      :rule="rule"
      :option="option"
      @submit="onSubmit"
    ></form-create>
  </template>
  <script setup lang=ts>
    const faps = ref(null)
    const rule = ref('')
    const option = ref('')
    const init = () => {
      rule.value = formCreate.parseJson('${ae.toJson(a).replaceAll("\\","\\\\")}')
      option.value = formCreate.parseJson('${JSON.stringify(e)}')
    }
    const onSubmit = (formData) => {
      //todo \u63D0\u4EA4\u8868\u5355
    }
    init()
  <\/script>`},x=a=>{let e="json";return u.value===2&&(e="xml"),M(a)||(a=JSON.stringify(a,null,2)),g.highlight(a,{language:e,ignoreIllegals:!0}).value||"&nbsp;"};return P(async()=>{g.registerLanguage("xml",q),g.registerLanguage("json",K)}),(a,e)=>{const p=oe,D=U("fc-designer"),F=T,I=ne,k=A,j=G("dompurify-html");return _(),w(ee,null,[o(F,{"body-style":{padding:"0px"},class:"!mb-0"},{default:n(()=>[m("div",se,[o(D,{class:"my-designer",ref_key:"designer",ref:t,config:s(C)},{handle:n(()=>[o(p,{size:"small",type:"primary",plain:"",onClick:O},{default:n(()=>e[2]||(e[2]=[c("\u751F\u6210JSON")])),_:1}),o(p,{size:"small",type:"success",plain:"",onClick:S},{default:n(()=>e[3]||(e[3]=[c("\u751F\u6210Options")])),_:1}),o(p,{size:"small",type:"danger",plain:"",onClick:J},{default:n(()=>e[4]||(e[4]=[c("\u751F\u6210\u7EC4\u4EF6")])),_:1})]),_:1},8,["config"])])]),_:1}),o(k,{modelValue:s(i),"onUpdate:modelValue":e[1]||(e[1]=y=>Z(i)?i.value=y:null),title:s(v),"max-height":"600"},{default:n(()=>[s(i)?(_(),w("div",te,[o(p,{style:{float:"right"},onClick:e[0]||(e[0]=y=>(async B=>{const N=JSON.stringify(B,null,2),{copy:R,copied:V,isSupported:z}=L({source:N});z?(await R(),s(V)&&h.success(d("common.copySuccess"))):h.error(d("common.copyError"))})(s(r)))},{default:n(()=>[c(X(s(d)("common.copy")),1)]),_:1}),o(I,{height:"580"},{default:n(()=>[m("div",null,[m("pre",null,[Y(m("code",le,null,512),[[j,x(s(r))]])])])]),_:1})],512)):Q("",!0)]),_:1},8,["modelValue","title"])],64)}}});export{ie as default};
