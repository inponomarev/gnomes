<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <a href class="navbar-brand" @click.prevent>Gnomes</a>

      <div class="navbar-nav mr-auto">
        <li class="nav-item">
          <router-link to="/home" class="nav-link">
            <font-awesome-icon icon="home"/>
            Home
          </router-link>
        </li>
        <li class="nav-item">
          <router-link v-if="currentUser" to="/user" class="nav-link"
          >User
          </router-link
          >
        </li>
      </div>

      <div v-if="!isLoggedIn" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/register" class="nav-link">
            <font-awesome-icon icon="user-plus"/>
            Sign Up
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/login" class="nav-link">
            <font-awesome-icon icon="sign-in-alt"/>
            Login
          </router-link>
        </li>
      </div>

      <div v-if="currentUser" class="navbar-nav ml-auto">
        <li class="nav-link">
            <font-awesome-icon icon="user"/>
            {{ currentUser }}
        </li>
        <li class="nav-item">
          <a class="nav-link" href @click.prevent="logOut">
            <font-awesome-icon icon="sign-out-alt"/>
            LogOut
          </a>
        </li>
      </div>
    </nav>

    <div class="container">
      <router-view/>
    </div>
  </div>
</template>

<script lang="ts">

import { defineComponent } from 'vue';
import { State } from '@/store/state';
import store from '@/store';
import { MutationType } from '@/store/mutations';

export default defineComponent({
  computed: {
    currentUser() {
      return (store.state as State).currentUser;
    },
    isLoggedIn() {
      return (store.state as State).loggedIn;
    },
  },

  methods: {
    logOut() {
      store.commit(MutationType.Logout);
      this.$router.push('/login');
    },
  },
});

</script>
