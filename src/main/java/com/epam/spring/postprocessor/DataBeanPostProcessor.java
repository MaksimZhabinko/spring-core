package com.epam.spring.postprocessor;

import com.epam.spring.annotation.BindStaticData;
import com.epam.spring.model.entity.Identifiable;
import com.epam.spring.util.IdGenerator;

import com.google.gson.Gson;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.json.JSONArray;
import org.springframework.util.ReflectionUtils;

@Slf4j
@Setter
public class DataBeanPostProcessor implements BeanPostProcessor {
    private IdGenerator idGenerator;
    private Gson gson;
    private String startInfoMessage;
    private String finishInfoMessage;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            BindStaticData annotation = field.getAnnotation(BindStaticData.class);
            if (Objects.nonNull(annotation) && Map.class.isAssignableFrom(field.getType())) {
                LOG.info(startInfoMessage);
                JSONArray jsonArray = new JSONArray(readFileFromResources(annotation.fileLocation()));
                Map<Long, Identifiable> hashMap  = instantiateCollectionFromJson(jsonArray, annotation.castTo());
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, hashMap);
                LOG.info(finishInfoMessage);
            }
        }
        return bean;
    }

    private <T extends Identifiable> Map<Long, T> instantiateCollectionFromJson(JSONArray jsonArray, Class<?> clazz) {
        HashMap<Long, T> hashMap = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String jsonObjectAsString = jsonObject.toString();
            T entity = (T) gson.fromJson(jsonObjectAsString, clazz);
            entity.setId(idGenerator.generateId(clazz));
            hashMap.put(entity.getId(), entity);
        }
        return hashMap;
    }

    private String readFileFromResources(String filename) {
        URL resource = DataBeanPostProcessor.class.getClassLoader().getResource(filename);
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return new String(bytes);
    }
}
