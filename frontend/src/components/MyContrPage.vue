<template class="main">
    <v-app> 
        <template>
            <h1 > Контракт {{ $route.params.name }}</h1>
        </template>  
        <v-divider></v-divider>
        <span margin-left="200px">

            <v-dialog 
                v-model="$store.state.dialog8"
                persistent
                max-width="1000px">
                <template v-slot:activator="{ on, attrs }"> 
                    <v-btn 
                        outlined  
                        icon color="blue" 
                        v-bind="attrs" 
                        v-on="on">
                            <v-icon >mdi-pencil</v-icon>
                    </v-btn>
                </template>
                <my-edit-contract></my-edit-contract>
            </v-dialog> 
            
            <v-btn @click="dialog2=true"
                class="btn"
                outlined
                icon color="red">
                <v-icon >mdi-delete</v-icon>
            </v-btn>
            <v-dialog v-model="dialog2"
                max-width="600px">
                <v-card>                   
                    <v-card-actions>
                        <router-link class="btn" :to="{name: 'contract'}">
                            <v-btn @click="dialog2=false, deleteContract(NameDelete)">Удалить</v-btn>
                        </router-link>                       
                        <v-btn @click="dialog2=false">Отмена</v-btn>
                        <router-link class="btn" :to="{name: 'contract'}">
                            <v-btn @click="dialog2=false, deleteContract(NameDelete), deleteWithContragent(NameCounterpartiesDelete)">Удалить c агентом</v-btn>
                        </router-link>
                    </v-card-actions>
                </v-card>
            </v-dialog>


            
        </span>
        <v-divider></v-divider>
        <v-container>
            <v-simple-table class="table">
                <thead >
                    
                    <th v-for="(item) in columns"
                        :key="item">
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
                    <td>{{ contract.phases.name }}</td>
                    
                    <td v-for="organiz in contract.contractCounterparties">{{ organiz.name}}</td>
                </tbody>
            </v-simple-table>
        </v-container>
        
        <v-divider></v-divider>
        <div v-for="(item, name, id) in contract.phases"  >
        <p>{{ item.amount }}</p>{{ contract.phases.amount }}</div>
        <span>   
            <router-link class="btn" :to="{name: 'contract'}">
                <v-btn color=" light"> Назад в контракты </v-btn>
            </router-link> 
        </span>
    </v-app>
</template>

<script>
import { mapGetters, mapMutations, mapActions } from 'vuex';
import MyCreateContract from '../components/MyCreateContract.vue';
import MyEditContract from '../components/MyEditContract.vue';
export default {
    name: 'contractPage',
    components: {
        MyCreateContract,
        MyEditContract, 
    },

    props: {
        
    },

    data() {
        return {
            contract: [],
            dialog1: false,
            dialog2: false,
            columns: [
                "Название ",
                "Тип",
                "Планируемая дата    начала  ",
                "Плановая дата окончания  ",
                "Фактическая дата начала  ",
                "Фактическа дата окончания  ",
                "Сумма  ",
                "Этапы контракта ",
                "Контрагенты  "         
            ],
            NameDelete: '',
            NameCounterpartiesDelete: '',
        }

    },

    computed:{
        ...mapGetters({           
            contracts: 'contracts',
        }),       
    },
    methods: {
        ...mapActions(['deleteContract'] ),
        ...mapActions(['deleteWithContragent'])
    }, 
    activated() {       
        for (let i = 0; i<this.contracts.length; i++ ){
            if (this.$route.params.name === this.contracts[i].name) {
                this.contract = this.contracts[i]
                console.log(this.contract)         
                this.NameDelete = this.contract.name
                this.NameCounterpartiesDelete = this.contract.contractCounterparties[0].name
                console.log(this.NameDelete)  
                console.log(this.NameCounterpartiesDelete)           
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
tr, th{
    width: auto;
    height: 50px;
}
.table {
    
    table-layout: fixed;
  width: 100%;
    border: 1px solid black;
}
.btn {
    margin-left:20px
}
</style>