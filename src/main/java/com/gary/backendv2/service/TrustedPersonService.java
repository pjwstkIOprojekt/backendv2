package com.gary.backendv2.service;

import com.gary.backendv2.repository.TrustedPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrustedPersonService {
	private final TrustedPersonRepository trustedPersonRepository;
}
