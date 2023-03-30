<template class="main">
    <v-app>
    
        <section >
        <my-menu></my-menu></section>
        <section class="content">
        <h1>Организации-Контрагенты</h1>
        <v-divider></v-divider>
        <v-container >
            <v-btn outlined  icon color="blue">
                <v-icon >mdi-plus</v-icon>
            </v-btn>
            Добавить
        </v-container>
         <v-divider></v-divider>
        <v-container>
            
        </v-container>
        </section> 
        organization 
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
    name: 'MyOrganization',
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