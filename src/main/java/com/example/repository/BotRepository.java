package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Bot;

public interface BotRepository extends JpaRepository<Bot, Long> {

}