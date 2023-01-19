package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.enums.BackupType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BackupResponse {
	private Integer backupId;
	private Boolean accepted;
	private String justification;
	private LocalDateTime time;
	private BackupType backupType;
}
