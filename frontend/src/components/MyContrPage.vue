<template class="main">
    <v-app> 
        <template>
            <h1> Контракт {{ $route.params.name }}</h1>
        </template>  
        <span>   
            <router-link class="btn" :to="{name: 'contract'}">
                <v-btn color=" light"> Назад в контракты </v-btn>
            </router-link> 
            
            <span v-for="val in contracts"><v-btn 
            @click="Aga()"
            >{{val.name}}</v-btn>
            {{  }}
        </span>
        </span>
        <span>
            <v-btn 
            @click="Aga">dfb</v-btn>
            {{ contracts[0].name }}
            {{ contracts.length }}
        </span>
    </v-app>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex';

export default {
    name: 'contractPage',
    components: {
        
    },

    props: {
        
    },

    data() {
        return {
            contract: [],
        }
    },

    computed:{
        ...mapGetters({           
            contracts: 'contracts/contracts',
        }),

    

        
            
    },
    methods: {
        Aga() {
            for (let i = 0; i<this.contracts.length; i++ ){
                if (this.$route.params.name === this.contracts[i].name) {
                    this.contract = this.contracts[i]
                    console.log(this.contract)
                    
                    break
                    
                }
                console.log(this.contracts[i].name)
                
            }
            
                console.log(1)
            }
    }
}

    </script>