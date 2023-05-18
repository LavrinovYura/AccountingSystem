<template >
    <v-app class="main">
        <v-card>
            <v-navigation-drawer height="400px" absolute permanent >
                <template v-slot:prepend>
                    <v-list-item two-line>
                        <v-list-item-icon>
                            <v-icon>
                                mdi-account
                            </v-icon> 
                        </v-list-item-icon>
                        <v-list-item-content>
                            <v-list-item-title>
                                {{ "name" }}
                            </v-list-item-title>
                            <v-list-item-subtitle>
                                {{ "name "}}
                            </v-list-item-subtitle>
                        </v-list-item-content>
                    </v-list-item>
                </template>
                <v-divider></v-divider>
                <v-list dense v-for='(items, index) in menuList'>
                    <router-link class="btn" :to="{name: items.name}">
                        <v-list-item>{{ items.item }}</v-list-item>
                    </router-link>
                </v-list>               
            </v-navigation-drawer>
        </v-card>
    </v-app>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
    name: 'MyMenu',

    props: {
        
    },

    data() {
        return {
            name: localStorage.name

        }
    },
    computed:{
        ...mapGetters({
            menuList: 'menu/menuList',
        }),
    }
}


</script>

<style  scoped>
.main {
    max-height: 400px;  
    max-width: 250px; 
}
</style>