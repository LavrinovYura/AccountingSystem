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
                            <span v-for="(field, index) in fields">
                                <template v-if="field.model==='password'">
                                    <v-text-field :placeholder=field.placeholder
                                    :type='pasType' 
                                    :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                    @click:append="showPas"
                                    v-model="user[field.model]"
                                    >
                                    </v-text-field>
                                </template>
                                <template v-else>
                                    <v-text-field :placeholder=field.placeholder
                                    v-model="user[field.model]"
                                    >
                                    </v-text-field>
                                </template>
                            </span>
                    </section>
                    </v-card-text>  
                    {{ user }}       
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
                fullname: '',
                names: '',
                data: 201,
                success: false,
                registr: '',    
                user: {
                    firstName: '',
                    secondName: '',
                    middleName: '',
                    password: "",
                    username: "",
                },
                fields:[
                    {placeholder: 'Имя', model: 'firstName'},
                    {placeholder: 'Фамилия', model: 'secondName'},
                    {placeholder: 'Отчество', model: 'middleName'},
                    {placeholder: 'Имя Пользователя', model: 'username'},
                    {placeholder: 'Пароль', model: 'password'}
                ]     
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
                        username: this.user.username,
                        password: this.user.password,
                        firstName: this.user.firstName,
                        secondName: this.user.secondName,
                        middleName: this.user.middleName,
                        
                    });
                    console.log(response)
                    console.log(response.data)            
                    if (response.status === this.data) {
                        this.success = true
                    }  
                    if  (this.success == true) {
                        this.addToken(response.data.accessToken)
                        this.addFirstName(response.data.firstName)
                        this.addSurName(response.data.secondName)
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
        }
    }
</script>
  
<style  scoped>
.btn {
    text-decoration: none
}
  
  
</style> 