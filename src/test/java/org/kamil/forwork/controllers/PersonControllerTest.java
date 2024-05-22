//package org.kamil.forwork.controllers;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.kamil.forwork.dtos.TransferMoneyDTO;
//import org.kamil.forwork.services.PersonService;
//import org.kamil.forwork.util.validators.TransferValidator;
//import org.kamil.forwork.util.validators.ValidatingTools;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.context.support.WithSecurityContext;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MockMvcBuilder;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.validation.BindingResult;
//
//import java.lang.annotation.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//@Slf4j
//class PersonControllerTest {
//    @Mock
//    private ValidatingTools validatingTools;
//    @Mock
//    private TransferValidator transferValidator;
//    @Mock
//    private PersonService personService;
//    @InjectMocks
//    private PersonController personController;
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//    private BindingResult bindingResult;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    @WithMockUser(username = "admin2")
//    void transfer() throws Exception {
//        TransferMoneyDTO transferMoneyDTO = new TransferMoneyDTO("admin5", "admin2", 1000.0);
//        String request = objectMapper.writeValueAsString(transferMoneyDTO);
//
//
//        mockMvc.perform(post("/person/transfer")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(request)).andExpect(status().isOk());
//        verify(personService, times(1)).transfer(transferMoneyDTO.getTargetUsername(),
//                transferMoneyDTO.getSenderUsername(), transferMoneyDTO.getAmount());
//    }
//
//}