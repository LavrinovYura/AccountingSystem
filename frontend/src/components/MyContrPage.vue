<template class="main">
    <v-app> 
        <template>
            <h1 > Контракт {{ $route.params.name }}</h1>
        </template>  
        <v-divider></v-divider>
        <span margin-left="200px">
            <v-btn                 
                outlined  
                icon color="blue" 
                class="mr-6 ml-10"
                >
                    <v-icon >mdi-pencil</v-icon>
            </v-btn>
            <v-btn class="btn"
                outlined  
                icon color="red" 
                >
                    <v-icon >mdi-delete</v-icon>
            </v-btn>
        </span>
        <v-divider></v-divider>
        <v-container>
            <v-simple-table>
                <thead >
                    
                    <th v-for="(item) in columns"
                        :key="item.id">
                        {{ item }}
                    </th>
                </thead>
                <tbody>
                    <td>{{ contract.name }}</td>
                    <td>{{ contract.type }}</td>
                    <td>{{ contract.plannedStartDate }}</td>
                    <td>{{ contract.plannedEndDate }}</td>
                    <td>{{ contract.actualStartDate }}</td>
                    <td>{{ contract.actualEndDate }}</td>
                    <td>{{ contract.amount }}</td>
                    <td>{{ contract.phases.length }}</td>
                    <td>{{ contract.contractCounterparties.length }}</td>
                    <td></td>
                </tbody>
            </v-simple-table>
        </v-container>
        
        <v-divider></v-divider>
        <span>   
            <router-link class="btn" :to="{name: 'contract'}">
                <v-btn color=" light"> Назад в контракты </v-btn>
            </router-link> 
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
            columns: [
                "Название ",
                "Тип",
                "Планируемая дата    начала  ",
                "Плановая дата окончания  ",
                "Фактическая дата начала  ",
                "Фактическа дата окончания  ",
                "Сумма  ",
                "Этапы  ",
                "Контрагенты  "         
            ],
        }

    },

    computed:{
        ...mapGetters({           
            contracts: 'contracts/contracts',
        }),       
    },
    methods: {
        
    }, 
    activated() {       
        for (let i = 0; i<this.contracts.length; i++ ){
            if (this.$route.params.name === this.contracts[i].name) {
                this.contract = this.contracts[i]
                console.log(this.contract)                   
                break                   
            }                
        }
    }
}

</script>

<style scoped>
h1 {
    text-align: center;
    margin-top: 5%;
}
td, tr{
    width: 1000px;
    height: 50px;
}
.tabb {
    width: 305px;
}

</style>