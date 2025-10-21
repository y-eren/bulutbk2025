package com.payment.worker.controller;

import org.springframework.web.bind.annotation.GetMapping;

class Health { @GetMapping("/healthz") String ok(){ return "ok"; } }
