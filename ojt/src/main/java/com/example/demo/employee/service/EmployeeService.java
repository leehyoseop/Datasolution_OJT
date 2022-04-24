package com.example.demo.employee.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.example.demo.employee.dto.DepartmentDTO;
import com.example.demo.employee.dto.DeptOrganizationDTO;
import com.example.demo.employee.dto.EmployeeDTO;
import com.example.demo.employee.dto.HobbyDTO;
import com.example.demo.employee.mapper.EmployeeMapper;

@Service
public class EmployeeService {
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	//validation check
	public Map<String, String> validateHandling(Errors errors) {
		Map<String, String> validatorResult = new HashMap<>();
		for (FieldError error : errors.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		return validatorResult;
	}
	
	//insert
	public void create(EmployeeDTO dto) {
		employeeMapper.insert(dto);
	}
	//search by name
	public List<EmployeeDTO> search(EmployeeDTO dto) {
		return employeeMapper.search(dto);
	}
	//select one
	public EmployeeDTO selectOne(String id) {
		return employeeMapper.selectOne(id);
	}
	//update Employee
	public void updateEmp(String id, EmployeeDTO dto) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("dto", dto);
		employeeMapper.updateEmp(map);
	}
	//delete for Update EmployeeHobby
	public void deleteEmpHob(String id) {
		employeeMapper.deleteEmpHob(id);
	}
	//delete
	public void delete(String id) {
		employeeMapper.delete(id);
	}
	//select all
	//	public List<EmployeeDTO> select() {
	//		return employeeMapper.select();
	//	}
	//Select Department
	public List<DepartmentDTO> selectDepartment(){
		return employeeMapper.selectDepartment();
	}
	//Select Hobby
	public List<HobbyDTO> selectHobby(){
		return employeeMapper.selectHobby();
	}
	//Select selectedDepartment
	public DepartmentDTO selectedDepartment(String id) {
		return employeeMapper.selectedDepartment(id);
	}
	//Select notselectedDepartment
	public List<DepartmentDTO> notselectedDepartment(String id) {
		return employeeMapper.notselectedDepartment(id);
	}
	//Select selectedHobby
	public List<HobbyDTO> selectedHobby(String id) {
		return employeeMapper.selectedHobby(id);
	}
	//Select notselectedhobby
	public List<HobbyDTO> notselectedHobby0() {
		return employeeMapper.notselectedHobby0();
	}
	public List<HobbyDTO> notselectedHobby1(String id1) {
		return employeeMapper.notselectedHobby1(id1);
	}
	public List<HobbyDTO> notselectedHobby2(String id1, String id2) {
		return employeeMapper.notselectedHobby2(id1, id2);
	}
	public List<HobbyDTO> notselectedHobby3(String id1, String id2, String id3) {
		return employeeMapper.notselectedHobby3(id1, id2, id3);
	}
	public List<HobbyDTO> notselectedHobby4(String id1, String id2, String id3, String id4) {
		return employeeMapper.notselectedHobby4(id1, id2, id3, id4);
	}
	public List<HobbyDTO> notselectedHobby5(String id1, String id2, String id3, String id4, String id5) {
		return employeeMapper.notselectedHobby5(id1, id2, id3, id4, id5);
	}
	//Insert EmployeeHobby
	public void insertEmpHob(String id, EmployeeDTO dto) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("dto", dto);
		employeeMapper.insertEmpHob(map);
	}
	//Update EmployeeHobby
	public void updateEmpHob(String id, String hobid) {
		employeeMapper.updateEmpHob(id, hobid);
	}
	//ID Duplicate check
	public int idDuplicateCheck(EmployeeDTO dto) {
		return employeeMapper.idDuplicateCheck(dto);
	}
	//Department Organization
	public List<DeptOrganizationDTO> getEmpDept() {
		return employeeMapper.getEmpDept();
	}
	
	/////참고
	
	// json 데이터
	public List<DeptOrganizationDTO> getJson() {
		List<DeptOrganizationDTO> Deptdto = employeeMapper.getJson();
	    for (DeptOrganizationDTO dto : Deptdto) {
	    	String NmList = dto.getDeptNm() + "," + dto.getEmpList();
	        dto.setEmpList(NmList);
	    }
	    return Deptdto;
	}
}
