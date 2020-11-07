import wheel  from './office'
let module={
    install(vue,weex){

        weex.registerModule('wheel',wheel)
    }
}
export  default module
