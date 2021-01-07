//package com.alexkirillov.alitabot;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class SecurityPageAccessTests {
//    @Test
//    @WithMockUser(roles = "USER")
//    public void loginWithRoleUserThenExpectAdminPageForbidden() throws Exception {
//        mockMvc.perform(get("/admin"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void loginWithRoleAdminThenExpectAdminContent() throws Exception {
//        mockMvc.perform(get("/admin"))
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("Custom administrator page.")));
//    }
//
//    @Test
//    public void loginWithRoleUserThenExpectIndexPageRedirect() throws Exception {
//        FormLoginRequestBuilder login = formLogin()
//                .user("user")
//                .password("pass");
//
//        mockMvc.perform(login)
//                .andExpect(authenticated().withUsername("user"))
//                .andExpect(redirectedUrl("/index"));
//    }
//
//    @Test
//    public void loginWithRoleAdminThenExpectAdminPageRedirect() throws Exception {
//        FormLoginRequestBuilder login = formLogin()
//                .user("admin")
//                .password("pass");
//
//        mockMvc.perform(login)
//                .andExpect(authenticated().withUsername("admin"))
//                .andExpect(redirectedUrl("/admin"));
//    }
//}
