<template class="main">
    <v-app>
    
        <section >
        <my-menu></my-menu></section>
        <section class="content">
        vkugk
        
        </section>
        fdgdth 
        <span>   
            <router-link class="btn" :to="{name: 'menu'}">
                <v-btn color=" light"> Назад</v-btn>
            </router-link> 
        </span>
    
</v-app>
</template>

<script>
import MyMenu from '../components/MyMenu.vue';
export default {
    name: 'MyAdmin',
    components: {
        MyMenu,
    },

    props: {
        
    },

    data() {
        return {


        }
    }
}
</script>