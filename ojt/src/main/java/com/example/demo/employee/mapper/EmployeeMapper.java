package com.example.demo.employee.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.employee.dto.DepartmentDTO;
import com.example.demo.employee.dto.DeptOrganizationDTO;
import com.example.demo.employee.dto.EmployeeDTO;
import com.example.demo.employee.dto.HobbyDTO;

@Mapper
public interface EmployeeMapper {
	
	public int insert(EmployeeDTO dto);
	
	public List<EmployeeDTO> search(EmployeeDTO dto);
	
	public EmployeeDTO selectOne(String id);
	
	//public List<EmployeeDTO> select();
	
	public int updateEmp(Map<String, Object> map);
	
	public void deleteEmpHob(String id);
	
	public int delete(String id);
	
	public List<DepartmentDTO> selectDepartment();
	
	public List<HobbyDTO> selectHobby();
	
	public DepartmentDTO selectedDepartment(String id);
	
	public List<DepartmentDTO> notselectedDepartment(String id);
	
	public List<HobbyDTO> selectedHobby(String id);
	
	public List<HobbyDTO> notselectedHobby0();
	public List<HobbyDTO> notselectedHobby1(String id);
	public List<HobbyDTO> notselectedHobby2(@Param("id1") String id1, @Param("id2") String id2);
	public List<HobbyDTO> notselectedHobby3(@Param("id1") String id1, @Param("id2") String id2, @Param("id3") String id3);
	public List<HobbyDTO> notselectedHobby4(@Param("id1") String id1, @Param("id2") String id2, @Param("id3") String id3, @Param("id4") String id4);
	public List<HobbyDTO> notselectedHobby5(@Param("id1") String id1, @Param("id2") String id2, @Param("id3") String id3, @Param("id4") String id4, @Param("id5") String id5);
	
	public int insertEmpHob(Map<String, Object> map);
	
	public void updateEmpHob(@Param("id") String id, @Param("hobid") String hobid);
	
	public int idDuplicateCheck(EmployeeDTO dto);
	
	public List<DeptOrganizationDTO> getEmpDept();
	
	/////Âü°í
	public List<DeptOrganizationDTO> getJson();
}
