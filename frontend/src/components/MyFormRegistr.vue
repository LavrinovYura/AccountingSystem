<template>
    <v-app>
        <v-form>
            <v-container>
                <v-card width="400px" class="mx-auto mt-5">
                    <v-card-title class="pb-0">
                        <h2>Регистрация</h2>
                    </v-card-title>
                    <v-card-text>
                        <section>
                            <v-text-field placeholder="Surname" 
                                name="surname"
                                v-model.trim="secondName"
                                
                            ></v-text-field>
                            <v-text-field placeholder="Name" 
                                name="name"
                                v-model.trim="firstName"
                            ></v-text-field>
                            <v-text-field placeholder="Middle name" 
                                name="Middle name"
                                v-model.trim="middleName"
                            ></v-text-field>
                        </section>
                        <v-text-field placeholder="Username" 
                            name="username"
                            v-model="username"                            
                        ></v-text-field>
                        <v-text-field  
                            placeholder="Password" 
                            :type='pasType' 
                            name="password"
                            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                            @click:append="showPas"
                            v-model="password"
                        ></v-text-field>  
                    </v-card-text>         
                    <v-card-actions>
                        <router-link class="btn" :to="{name: 'FormEnter'}">
                            <v-btn color=" light"> Назад</v-btn>
                        </router-link> 
                        <router-link class='btn' :to="this.registr">
                            <v-btn @mousedown="registrate()" 
                                @click="add()"
                                class="buttn" 
                                color="success"
                                > Зарегестрироваться</v-btn>
                        </router-link> 
                        <router-link  :to="{name: 'menu'}">
                            <v-btn 
                                class="buttn" 
                                color="success"
                                @click=addLocal()
                                > ghbdth</v-btn>
                        </router-link>
                    </v-card-actions>
                </v-card>
            </v-container>
        </v-form>
    </v-app>
</template>
  
<script>
import { mapGetters, mapMutations } from 'vuex';
import axios from 'axios';

    export default {
        name: 'MyFormEnter',
    
        props: {
            
        },
    
        data() {
            return {
                showPassword: false,
                pasType: 'password',
                firstName: '',
                secondName: '',
                middleName: '',
                password: "",
                username: "",
                fullname: '',
                names: '',
                data: 201,
                success: false,
                registr: '',               
            }
        },
        
        methods: {
            ...mapMutations({
            addToken: 'ADD_TOKEN',
            addName: 'ADD_NAME'}),

            showPas() {
                this.showPassword = !this.showPassword
                this.pasType = this.pasType === "password" ? "text" : "password"               
            },
            
            async registrate() {
                try {
                    const response = await axios.post(this.$store.state.url + '/api/auth/register', {
                        username: this.username,
                        password: this.password,
                        firstName: this.firstName,
                        secondName: this.secondName,
                        middleName: this.middleName,
                        
                    });
                    console.log(response)
                    console.log(response.data)            
                    if (response.status === this.data) {
                        this.success = true
                    }  
                    if  (this.success == true) {
                        this.addToken(response.data.accessToken)
                        this.addName(response.data.fullName)
                }                      
                } 
                catch(e) {
                    alert('Неверно ')
                }      
            },

            goToMenu() {
            if (this.success == true) {
                this.$router.push('menu')   
            }           
            console.log(1)
        },
        
        add() {
            setTimeout(this.goToMenu, 1000)           
        },
        addLocal() {
            localStorage.setItem('tok', '50');
        },
        

        Name() {
            this.names = this.fullname.split(' ', 2)
            return this.names
        },
    
        }
    }
</script>
  
<style  scoped>
.btn {
    text-decoration: none
}
  
  
</style> 