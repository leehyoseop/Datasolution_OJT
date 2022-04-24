package com.example.demo.employee.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.employee.dto.DepartmentDTO;
import com.example.demo.employee.dto.DeptOrganizationDTO;
import com.example.demo.employee.dto.EmployeeDTO;
import com.example.demo.employee.dto.HobbyDTO;
import com.example.demo.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import oracle.sql.json.OracleJsonArray;

@Controller
@RequiredArgsConstructor
public class EmployeeController {
	
	private final EmployeeService employeeservice;
	
	//메인페이지
	@GetMapping("/employeeControl")
	public String employeeControl() {
		return "employee/employeeControlView";
	}

	//조직도
	@GetMapping("/departmentOrganization")
		   public String departmentControl(Model model) {
		      List<DeptOrganizationDTO> deptOrganizationList = employeeservice.getEmpDept();      
		      //Map<String, Object> data = new HashMap<String, Object>();
		      String json = null;
		      ModelAndView mav = new ModelAndView();
		      JSONArray Ljarray = new JSONArray();
		      
		      ArrayList<JSONObject> work = new ArrayList<JSONObject>();
		      try {
		         for (DeptOrganizationDTO dto : deptOrganizationList) {
		            JSONObject jo1 = new JSONObject();
		            JSONObject jo2 = new JSONObject();
		            JSONObject jo3 = new JSONObject();
		            JSONArray jarray = new JSONArray();
		           
		            jo1.put("v", dto.getDeptId());
		            String f = dto.getDeptNm() + "," + (dto.getEmpList() + "@");
		            jo1.put("f", f);
		            jo2.put("", dto.getUpdeptId());
		            jo3.put("", dto.getDeptNm());
		            jarray.add(jo1);
		            jarray.add(jo2);
		            jarray.add(jo3);
		            Ljarray.add(jarray);
		            //System.out.println(Ljarray);
		         }

		         json = new ObjectMapper().writeValueAsString(Ljarray);
		         json = json.replace("\"\":", "");
		         json = json.replace(",{", ",");
		         json = json.replace("}", "");
		         json = json.replace("@\"", "\"}");
		         System.out.println(json);  

//		         mav.addObject("myjsonarr",json);
//		         mav.setViewName("employee/departmentOrganization.html");
		         model.addAttribute("myjsonarr",json);
		         return "employee/departmentOrganization";
		      } catch (Exception e) {
		         // TODO: handle exception
		      }
		      return "";
		   }
		   
		   private char[] type(String json) {
		      // TODO Auto-generated method stub
		      return null;
		   }

	//사원 신규등록 페이지 생성
	@GetMapping("/employees/new")
	public String createForm(Model model) {
		//부서 기본값 정보 가져오기
		List<DepartmentDTO> deptList = employeeservice.selectDepartment();
		model.addAttribute("deptlist", deptList);
		//취미 기본값 정보 가져오기
		List<HobbyDTO> hobbyList = employeeservice.selectHobby();
		model.addAttribute("hobbylist", hobbyList);
		return "employee/employeeCreateView";
	}
	
	//사원 신규등록 페이지 -> DB등록
		@PostMapping("/employees/new")
		public String insertEmployee(@Valid EmployeeDTO dto, Errors errors, Model model) {
			//ID 및 이름 유효성 검사
			if (errors.hasErrors()) {
				Map<String, String> validatorResult = employeeservice.validateHandling(errors);
				for (String key : validatorResult.keySet()) {
					model.addAttribute(key, validatorResult.get(key));
				}
				//부서 기본값 정보 가져오기
				List<DepartmentDTO> deptList = employeeservice.selectDepartment();
				model.addAttribute("deptlist", deptList);
				//취미 기본값 정보 가져오기
				List<HobbyDTO> hobbyList = employeeservice.selectHobby();
				model.addAttribute("hobbylist", hobbyList);
				return "employee/employeeCreateView";
			} else {
				//ID 중복검사
				String Msg = "등록 완료";
				if (employeeservice.idDuplicateCheck(dto) >= 1) {
					Msg = "ID 중복. 재등록바랍니다.";
					model.addAttribute("msg", Msg);
				} else {
					//Employee 테이블에 정보 등록
					employeeservice.create(dto);
					//EmployeeHobby 테이블에 정보 등록
					if (dto.getHobbies() != null) {
						String[] emphobarr = dto.getHobbies().split(",");
						for (int i = 0; i < emphobarr.length; i++) {
							employeeservice.insertEmpHob(emphobarr[i], dto);
						}
					}
					model.addAttribute("msg",Msg);
				}
				//해당페이지에서 재등록 할 수 있도록
				//부서 기본값 정보 가져오기
				List<DepartmentDTO> deptList = employeeservice.selectDepartment();
				model.addAttribute("deptlist", deptList);
				//취미 기본값 정보 가져오기
				List<HobbyDTO> hobbyList = employeeservice.selectHobby();
				model.addAttribute("hobbylist", hobbyList);
				return "employee/employeeCreateView";				
			}
		}
		
	//이름 검색
	@GetMapping("/employees/search")
	public String searchEmployee(EmployeeDTO dto, Model model) {
		List<EmployeeDTO> employeeList = employeeservice.search(dto);
		model.addAttribute("employeelist", employeeList);
		return "employee/employeeControlView";
	}
	
	//사원 상세정보 페이지 정보 가져오기 -> 업데이트, 삭제를 위함
	@GetMapping("/employees/update/{id}")
	public String updateEmployee(@PathVariable String id, Model model) {
		//사원 기본값 정보 가져오기
		EmployeeDTO selectedEmployee = employeeservice.selectOne(id);
		model.addAttribute("selectedemployee", selectedEmployee);
		//부서 정보 가져오기1 - 해당 사원이 속해 있는 부서 정보
		DepartmentDTO selDept = employeeservice.selectedDepartment(id);
		model.addAttribute("seldept", selDept);
		//부서 가져오기2 - 해당 사원이 속해 있지 않은 부서 정보
		String seldeptid = selDept.getDeptId();
		List<DepartmentDTO> notselDeptList = employeeservice.notselectedDepartment(seldeptid);
		model.addAttribute("notseldeptlist", notselDeptList);
		//취미 가져오기1 - 해당 사원이 선택한 취미 정보
		List<HobbyDTO> selHobbyList = employeeservice.selectedHobby(id);
		model.addAttribute("selhobbylist", selHobbyList);
		//취미 가져오기2 - 해당 사원이 선택하지 않은 취미 정보
		//취미 개수의 변화에 따라 각 if-else 구문 내부로 할당
		List<HobbyDTO> notselHobbyList = new ArrayList<HobbyDTO>();
		String id1, id2, id3, id4, id5;
		if (selHobbyList.size() == 1) {//취미 1개 선택
			id1 = selHobbyList.get(0).getHobId();
			notselHobbyList = employeeservice.notselectedHobby1(id1);
			model.addAttribute("notselhobbylist", notselHobbyList);
		} else if (selHobbyList.size() == 2) {//취미 2개 선택
			id1 = selHobbyList.get(0).getHobId();
			id2 = selHobbyList.get(1).getHobId();			
			notselHobbyList = employeeservice.notselectedHobby2(id1, id2);
			model.addAttribute("notselhobbylist", notselHobbyList);
		} else if (selHobbyList.size() == 3) {//취미 3개 선택
			id1 = selHobbyList.get(0).getHobId();
			id2 = selHobbyList.get(1).getHobId();
			id3 = selHobbyList.get(2).getHobId();
			notselHobbyList = employeeservice.notselectedHobby3(id1, id2, id3);
			model.addAttribute("notselhobbylist", notselHobbyList);
		} else if (selHobbyList.size() == 4) {//취미 4개 선택
			id1 = selHobbyList.get(0).getHobId();
			id2 = selHobbyList.get(1).getHobId();
			id3 = selHobbyList.get(2).getHobId();
			id4 = selHobbyList.get(3).getHobId();			
			notselHobbyList = employeeservice.notselectedHobby4(id1, id2, id3, id4);
			model.addAttribute("notselhobbylist", notselHobbyList);
		} else if (selHobbyList.size() == 5) {//취미 5개 선택
			id1 = selHobbyList.get(0).getHobId();
			id2 = selHobbyList.get(1).getHobId();
			id3 = selHobbyList.get(2).getHobId();
			id4 = selHobbyList.get(3).getHobId();
			id5 = selHobbyList.get(4).getHobId();			
			notselHobbyList = employeeservice.notselectedHobby5(id1, id2, id3, id4, id5);
			model.addAttribute("notselhobbylist", notselHobbyList);
		} else if (selHobbyList.size() == 0) {//취미 0개 선택
			notselHobbyList = employeeservice.notselectedHobby0();
			model.addAttribute("notselhobbylist", notselHobbyList);
		}
		return "employee/employeeUpdateView";
	}
	
	//사원 상세정보 페이지 -> 업데이트
	@PutMapping("/employees/{id}")
	public String updateEmployee(@PathVariable String id, EmployeeDTO dto) {
		//Employee 테이블에 정보 등록
		employeeservice.updateEmp(id, dto);
		//EmployeeHobby 테이블에 정보 등록 -> 기존 정보 삭제 후 재등록 
		employeeservice.deleteEmpHob(id);
		if (dto.getHobbies() != null) {
			String[] emphobarr = dto.getHobbies().split(",");
			for (int i = 0; i < emphobarr.length; i++) {
				employeeservice.updateEmpHob(id, emphobarr[i]);
			}			
		}
		return "employee/employeeControlView";
	}
	
	//사원 상세정보 페이지 -> 삭제
	@DeleteMapping("/employees/{id}")
	public String deleteEmployee(@PathVariable String id) {
		employeeservice.delete(id);
		return "employee/employeeControlView";
	}
	
	//select all
	//	@GetMapping("/employees")
	//	public String getEmployees(Model model) {
	//		List<EmployeeDTO> employeeList = employeeservice.select(); 	
	//		model.addAttribute("employeeList", employeeList);
	//		return "employee/employeeListView";
	//	}
	
	//select one
	@GetMapping("/employees/{id}")
	public String getEmployee(@PathVariable String id, Model model) {
		EmployeeDTO selectedEmployee = employeeservice.selectOne(id);
		model.addAttribute("employeelist", selectedEmployee);
		return "employee/employeeControlView";
	}
}
