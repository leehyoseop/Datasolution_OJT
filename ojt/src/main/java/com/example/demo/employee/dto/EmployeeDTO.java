package com.example.demo.employee.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO {
	@NotBlank(message = "ID�� �ʼ��Է� ���Դϴ�.")
	private String empId;
	@NotBlank(message = "�̸��� �ʼ��Է� ���Դϴ�.")
	private String empNm;
	private String deptId;
	private String telNo;
	private String addr;
	private String hobbies;
}
