<template>
  <v-app>
    
    <v-form>
        <v-container>
            <v-card width="400px" class="mx-auto mt-5">
                <v-card-title class="pb-0">
                    <h2>Вход в учетную запись</h2>
                </v-card-title>
                <v-card-text>
                    
                        <v-text-field placeholder="Username" 
                            prepend-icon="mdi-account-circle" 
                            name="username"
                            v-model="username"
                            @click="err=false"
                            
                        ></v-text-field>
                        <p v-if="err" class="er">Неверный логин или пароль</p>
                        <v-text-field  
                            placeholder="Password" 
                            prepend-icon="mdi-lock" 
                            :type='pasType' 
                            name="password"
                            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                            @click:append="showPas"
                            v-model="password"
                            
                        ></v-text-field>
                    
                </v-card-text>
               {{ success }} {{ registr }}
                <v-card-actions>
                    <router-link class="btn" :to="this.registr">
                        <v-btn class="mr-6" color="success"
                            @mousedown="enter()"
                            @click="Add()"
                        >Вход</v-btn>
                    </router-link>
                    <router-link class="btn" :to="'registration'">
                        <v-btn color="info"> Регистрация</v-btn>
                    </router-link> 
                </v-card-actions>
            </v-card>
        </v-container>

    </v-form>
  </v-app>

</template>

<script>
import axios from 'axios';
export default {
    name: 'MyFormEnter',
    props: {
        
    },

    data() {
        return {
            showPassword: false,
            pasType: 'password',
            password: "",
            username: "",
            success: false,
            token: '',
            registr: '',
            err: false,
            color: '',
            
            
        }
    },
    
    methods: {   
        showPas() {
            this.showPassword = !this.showPassword
            this.pasType = this.pasType === "password" ? "text" : "password"           
        },
        
        async enter() {
            try {
                const response = await axios.post(this.$store.state.url + this.$store.state.urlEnter, {
                    username: this.username,
                    password: this.password,                    
               });
                console.log(response)
                console.log(response.data.tokenType)

                if (response.status === 200) {
                        this.success = true                       
                    } 
                if  (this.success == true) {
                    this.token = response.data.accessToken
                }                
            } 
            catch(error) {
               this.err=true
               this.username=''
               this.password=''
               this.color='red'               
            } 
            
        },
        
        GoToMenu() {
            if (this.success == true) {
                this.$router.push('menu')                
            }            
            console.log(1)
        },
        
        Add() {
            setTimeout(this.GoToMenu, 1000)            
        }
    },
    

    computed: {
        
        }
             
}
</script>



<style>
form {
    text-align: center;  
}
.btn {
    text-decoration: none
}
.er {
    color: red;
    font-size: 90%;
    margin-top: -10px;
}
</style>