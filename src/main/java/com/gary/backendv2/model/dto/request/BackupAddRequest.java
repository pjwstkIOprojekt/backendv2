package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.BackupType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BackupAddRequest extends BaseRequest {
	@Email
	private String requester;
	@NotNull
	private Integer incidentId;
	@NotNull
	private Boolean accepted;
	@NotBlank
	private String justification;
	@NotNull
	@Enumerated(EnumType.STRING)
	private BackupType backupType;
}
