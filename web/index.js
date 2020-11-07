import module  from  './module'
import component  from  './component'
let webPlugin={
    install(vue,weex){
        module.install(vue,weex)
        component.install(vue,weex)
    }
}
export  default webPlugin
