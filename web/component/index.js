import office  from './office'
let component={
    install(vue,weex){
        weex.registerComponent('office',office)
    }
}
export  default component
