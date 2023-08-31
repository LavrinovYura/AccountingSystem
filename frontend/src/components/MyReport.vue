<template class="main">
    <v-app>
    
    <section >
        <my-menu></my-menu>
    </section>
    <div class="content">
        <h1>Отчеты</h1>
        <v-divider></v-divider>
        <v-container >
            <h5>Поиск контрактов в временном интервале</h5>
            <v-divider></v-divider>
            <v-row >
                <v-col>
                    <label text-color="green"> Дата начала
                        <v-text-field type="date">
                        </v-text-field></label></v-col>
                <v-col> 
                    <label> Дата окончания
                        <v-text-field type="date">
                        </v-text-field>
                    </label>
                </v-col>
            </v-row>
            <v-btn class="btn" color="green" outlined > Скачать</v-btn>
        </v-container>
        <v-divider></v-divider>
        <v-container>
            <h5>Поиск контракта</h5>
            <label>Введите название контракта
                <v-text-field></v-text-field>
                <v-btn class="btn" color="green" outlined > Скачать</v-btn>
            </label>
            
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
            plannedStartDate: '',
            plannedEndDate: '',

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