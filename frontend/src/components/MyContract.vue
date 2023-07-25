<template class="main">
    <v-app>   
        <section >
        <my-menu></my-menu>
        </section>
        <section class="content" >
        <h1>Контракты</h1>
        <v-divider></v-divider>
            <v-container>
                <v-row>
                    <v-dialog 
                        v-model="$store.state.dialog1"
                        persistent
                        max-width="1000px">
                        <template v-slot:activator="{ on, attrs }"> 
                            <v-btn 
                                outlined  
                                icon color="blue" 
                                v-bind="attrs" 
                                v-on="on">
                                    <v-icon >mdi-plus</v-icon>
                            </v-btn>
                        </template>
                        <my-create-contract></my-create-contract>
                    </v-dialog>               
                    <template>
                        <v-btn class="btn"
                            outlined  
                            icon color="blue" 
                            >
                                <v-icon >mdi-magnify</v-icon>
                        </v-btn>
                    </template>               
                </v-row>
            </v-container>        
            <v-divider></v-divider>
            <v-container>
                <v-simple-table height="700px" class="table" >
                    <template  v-slot:default>
                        <thead>
                            <th v-for="item in headers" :key="item.text">
                                {{ item.text }}
                            </th>
                        </thead>
                        <tbody>
                            <tr v-for="(item) in contracts"
                                :key="item.idndex"
                                @click="Show(item)"
                                >
                                <td>{{ item.name}}</td>
                                <td>{{ item.type }}</td>
                                <td>{{ item.plannedStartDate }}</td>
                                <td>{{ item.plannedEndDate }}</td>
                                <td>{{ item.actualStartDate }}</td>
                                <td>{{ item.actualEndDate }}</td>
                                <td>{{ item.amount }}</td>
                                <td><div v-for="items in item.phases">{{ items.name }}</div></td>
                                <td>{{ item.contractCounterparties[0].name }}</td>
                                <td>
                                    <router-link class="btn" :to="{name:'contractPage', params: {name: `${item.name}`}}">
                                        <v-btn outlined small depressed>
                                            Открыть
                                        </v-btn>
                                    </router-link>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>           
            </v-container>
        </section>
        {{ $store.state.token }}
        <span>   
            <router-link class="btn" :to="{name: 'menu'}">
                <v-btn color=" light"> Назад в меню </v-btn>
            </router-link> 
        </span>       
        <span>
            <v-btn
            @click="getContractt()">
            download</v-btn>
        </span>  
        {{ testLocal }}{{ $store.state.tokenType }}
    </v-app>
</template>

<script>
import { mapGetters, mapMutations,mapActions } from 'vuex';
import MyMenu from '../components/MyMenu.vue';
import MyCreateContract from '../components/MyCreateContract.vue';
export default {
    name: 'MyContract',
    components: {
        MyMenu,
        MyCreateContract,
    },

    props: {
        
    },

    data() { 
        return {           
            dialog2: false,
            newContract:
                {
                    name: '',
                    type: '',
                    plannedStartDate: '',
                    plannedEndDate: '',
                    actualStartDate: '',
                    actualEndDate: '',
                    amount: '',
                    phases: [{ 
                        name: '',
                        amount: '',
                        planAmount: '',
                        factAmount: '',
                        plannedStartDate: '',
                        plannedEndDate: '',
                        actualStartDate: '',
                        actualEndDate: '',
                    }],
                    contractCounterparties: [{ 
                        name: '',
                        amount: '',
                        organization: '',
                        type: '',
                        plannedStartDate: '',
                        plannedEndDate: '',
                        actualStartDate: '',
                        actualEndDate: '',
                    }]
                },
            testLocal: localStorage.tok,
        }
    },
    computed:{
        ...mapGetters({
            headers: 'contractsModul/headers',           
            contracts: 'contracts',
            type: 'contractsModul/type',
        
        }),
        
    },

    methods: {
        ...mapMutations({
            addNewContract: 'ADD_NEW_CONTRACT',
            addAllContracts: 'ADD_ALL_CONTRACTS'
        }),
        sndNewContract() {
            this.dialog1 = false;
            const res = {};
            for (let item in this.newContract) {
               res[item] = this.newContract[item];
            }
            this.addNewContract(res);
            for (let item in this.newContract) {
               this.newContract[item] = '';
            };
               
        },
        Show(item) {
            console.log(item.name)
        },

        async getContractt() {
            try {
                const response = await axios.get(this.$store.state.url + '/api/menu/contracts/show', 
                {headers: {
                    "Authorization":  "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbm5ubmEifQ.8uH8-TkwiqueuYwOaA7VpFpjQZyvXZbgJWyBc99tN2E",               
                }})
                console.log(response)
                for (let i = 0; i<response.data.length; i++)  {
                    console.log(i);
                    this.addAllContracts(response.data[i])
                    console.log(response.data[i]);                   
                }                         
            } 
            catch(e) {
                alert('Error is true')
            }      
        },
    },

    activated() {
        this.getContractt()
    }
}

</script>

<style scoped>
.content {
    padding-left: 280px;
    padding-top: 20px;
    position: fixed;
}
.btn {
    margin-left:20px
}
.table {
    border: 1px solid black;
}
th {
    width: 250px;
}
</style>

