<template>
  <div class="col-md-12">
    <div class="card card-container">
      <img
        id="profile-img"
        src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
        class="profile-img-card"
      />
      <form name="form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">Username</label>
          <input
            id="username"
            v-model="user.login"
            type="text"
            class="form-control"
          />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="user.password"
            type="password"
            class="form-control"
          />
        </div>
        <div class="form-group">
          <button class="btn btn-primary btn-block" :disabled="loading">
            <span
              v-show="loading"
              class="spinner-border spinner-border-sm"
            ></span>
            <span>Login</span>
          </button>
        </div>
        <div class="form-group">
          <div v-if="message" class="alert alert-danger" role="alert">
            {{ message }}
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts">
import { API } from '@/apiconf';
import { defineComponent } from 'vue';
import { LoginDTO } from '@/apiclient';
import { MutationType } from '@/apistore/mutations';
import apiStore from '@/apistore';

export default defineComponent({
  data() {
    return {
      user: {
        login: '',
        password: '',
      } as LoginDTO,
      loading: false,
      message: '',
    };
  },

  methods: {
    async handleLogin() {
      this.loading = true;
      try {
        const response = await API.login({
          login: this.user.login,
          password: this.user.password,
        } as LoginDTO);
        apiStore.commit(MutationType.Login, response.data);
        API.updateAccessToken();
        this.loading = false;
        this.$router.push('/user');
      } catch (err) {
        this.loading = false;
        this.message = err.toString();
      }
    },
  },
});
</script>

<style scoped>
label {
  display: block;
  margin-top: 10px;
}

.card-container.card {
  max-width: 350px !important;
  padding: 40px 40px;
}

.card {
  background-color: #f7f7f7;
  padding: 20px 25px 30px;
  margin: 0 auto 25px;
  margin-top: 50px;
  -moz-border-radius: 2px;
  -webkit-border-radius: 2px;
  border-radius: 2px;
  -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}

.profile-img-card {
  width: 96px;
  height: 96px;
  margin: 0 auto 10px;
  display: block;
  -moz-border-radius: 50%;
  -webkit-border-radius: 50%;
  border-radius: 50%;
}
</style>
