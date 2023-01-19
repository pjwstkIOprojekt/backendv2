package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.BackupType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BackupUpdateRequest extends BaseRequest {
	@NotNull
	private Boolean accepted;
	@NotBlank
	private String justification;
	@NotNull
	private BackupType backupType;
}
