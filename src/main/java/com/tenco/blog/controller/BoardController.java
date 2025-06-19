package com.tenco.blog.controller;

import com.tenco.blog.model.Board;
import com.tenco.blog.repository.BoardNativeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller // IoC 대상 - 싱글톤 패턴으로 관리 됨
public class BoardController {

    private BoardNativeRepository boardNativeRepository;

    // DI: 의존성 주입 : 스프링이 자동으로 객체를 주입
    public BoardController(BoardNativeRepository boardNativeRepository) {
        this.boardNativeRepository = boardNativeRepository;
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable (name = "id") Long id) {
        // 클라이언트 --> 삭제 요청 처리 --> 응답: 리다이렉트 -- 클라이언트 --> / --> 응답

        boardNativeRepository.deleteById(id);
        // PRG 패턴 (Post-Redirect-Get) 적용
        // 삭제 후 메인 페이지로 리다이렉트 하여 중복 삭제 방지
        // 새로고침을 해도 삭제가 다시 실행되지 않음
        return "redirect:/";

    }

    /**
     * 상세보기 화면 요청
     * board/1
     */
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, HttpServletRequest request) {

        Board board = boardNativeRepository.findById(id);
        request.setAttribute("board",board);

        return "board/detail";

    }

    @PostMapping("/board/save")
    // username, title, content <-- DTO 받는 방법, 기본 데이터 타입 설정
    // form 태그에서 넘어오는 데이터 받기
    // form 태그에 name 속성의 key 값이 동일해야함
    // http://localhost:8080/board/save
    // 스프링 부트 기본 파싱 전략 - key=value (form)
    public String save(@RequestParam("title") String title,
                       @RequestParam("content") String content,
                       @RequestParam("username") String username) {
        System.out.println("title : " + title);
        System.out.println("content : " + content);
        System.out.println("username : " + username);

        boardNativeRepository.save(title, content, username);


        return "redirect:/";
    }


    // 리스트 목록 보여주기
    @GetMapping({"/","/index"})
    // public String index(Model model) {
    public String index(HttpServletRequest request) {

        // DB 접근해서 select 결과값을 받아서 머스태치 파일에 데이터 바인딩 처리
        List<Board> boardList = boardNativeRepository.findAll();
        // 뷰에 데이터를 전달 -> Model 사용가능
        request.setAttribute("boardList", boardList);

        return "index";
    }

    @GetMapping("/board/save-form")
    public String saveForm() {
        // /templates/board
        // /templates/board/
        return "/board/save-form";
    }

}
