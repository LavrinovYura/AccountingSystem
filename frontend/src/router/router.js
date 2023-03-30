import Vue from "vue";
import Router from "vue-router";

import MyFormEnter from '../components/MyFormEnter'
import MyFormRegistr from '../components/MyFormRegistr';
import MyContract from '../components/MyContract';
import MyMenu from '../components/MyMenu';
import MyOrganization from '../components/MyOrganization';
import MyReport from '../components/MyReport';
import MyAdmin from '../components/MyAdmin';
import MyContrPage from '../components/MyContrPage';

Vue.use(Router);

let router = new Router({
    routes: [
        {
            path: '/',
            name: 'FormEnter',
            component: MyFormEnter,
            props: true
            
        },
        {
            path: '/Registration',
            name: 'registration',
            component: MyFormRegistr,
            props: true
        },
        {
            path: '/Contracts',
            name: 'contract',
            component: MyContract,
            props: true
        },
        {
            path: '/Menu',
            name: 'menu',
            component: MyMenu,
            props: true
        },
        {
            path: '/Organizations',
            name: 'organization',
            component: MyOrganization,
            props: true
        },
        {
            path: '/Reports',
            name: 'report',
            component: MyReport,
            props: true
        },
        {
            path: '/Admin',
            name: 'admin',
            component: MyAdmin,
            props: true
        },
        {
            path: '/Contracts/:name',
            name: 'contractPage',
            component: MyContrPage,
            props: true
        },
        
        

    ],
    mode: 'history'
})

export default router;