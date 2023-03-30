<template class="main">
    <v-app>
    
        <section >
        <my-menu></my-menu>
    </section>
        <section class="content">
        <h1>Отчеты</h1>
        <v-divider></v-divider>
        <v-container >
            <v-btn outlined  icon color="blue">
                <v-icon >mdi-plus</v-icon>
            </v-btn>
            Добавить
        </v-container>
         <v-divider></v-divider>
        <v-container>
            <v-text-field v-model="search"
            append-icon="mdi-magnify"
            label="Search"
            single-line
            hide-details>
            </v-text-field>
            <v-simple-table>
                <thead>
                    <tr class="text-center">
                        <th> </th>

                    </tr>
                </thead>
            </v-simple-table>
            
        </v-container>
        </section>
        report
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