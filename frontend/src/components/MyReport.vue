<template class="main">
    <v-app>
    
    <section >
        <my-menu></my-menu>
    </section>
    <div class="content">
        <h1>Отчеты</h1>
        <v-divider></v-divider>
        <v-container >
            <v-btn outlined  icon color="blue">
                <v-icon >mdi-plus</v-icon>
            </v-btn>
            <v-btn class="btn" color="green" outlined > Скачать</v-btn>
        </v-container>
        
        <v-divider></v-divider>
        
    </div> 
    reports 
    <span>   
        <router-link  :to="{name: 'menu'}">
            <v-btn class="btn" color=" light"> Назад</v-btn>
        </router-link> 
    </span>

</v-app>
</template>

<script>
import MyMenu from '../components/MyMenu.vue';
export default {
    name: 'MyReport',
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

<style  scoped> 
.main {
    max-height: 400px;  
    max-width: 250px; 
}
.content {
    padding-left: 280px;
    padding-top: 20px;
    position: fixed;
}
.btn {
    margin-left:20px;
    margin-right: 20px;
}
h1 {
    text-align: center;
}
</style>