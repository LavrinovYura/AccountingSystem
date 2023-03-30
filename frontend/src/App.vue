<template>
  <v-app>
    
    <v-main>
      <keep-alive>
        <router-view></router-view>
      </keep-alive>
      
 <!-- --> 
    </v-main>
  </v-app>
</template>

<script>

export default {
  name: 'App',
  data()  {
    return {
      
    }
    //
  },
};
</script>
