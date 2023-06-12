package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.domain.BoardDto;
import com.fastcampus.ch4.domain.PageHandler;
import com.fastcampus.ch4.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @PostMapping("/write")
    public String write(BoardDto boardDto, Model m, HttpSession session, RedirectAttributes rattr){
        //getAttribute가 반환하는 값이 Object여서 형변환 필요
        String writer = (String) session.getAttribute("id");
        boardDto.setWriter(writer);

        try {
            int rowCnt = boardService.write(boardDto); //insert

            if(rowCnt!=1){
                throw new Exception("write failed");
            }
            rattr.addFlashAttribute("msg", "WRT_OK");

            return "redirect:/board/list"; //redirect는 무조건 get 형식이다. 여기서 list는 get형식
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("boardDto", boardDto);
            rattr.addFlashAttribute("msg", "WRT_OK");
            return "board";
        }
    }
    @GetMapping("/write")
    public String write(Model m){
        m.addAttribute("mode", "new");

        return "board"; //읽기와 쓰기로 사용, 쓰기에 사용할때는 mode값을 new로 지정
    }

    @PostMapping("/remove")
    public String remove(Integer bno, Integer page, Integer pageSize, Model m, HttpSession session, RedirectAttributes rattr){
        String writer = (String)session.getAttribute("id");

        //삭제할시 작성자 필요함
        try {
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);

            int rowCnt =boardService.remove(bno, writer);

            //삭제에 성공하면 1, msg를 전달
            if(rowCnt !=1) {
                throw new Exception("board remove error");
            }{
                rattr.addFlashAttribute("msg", "DEL_OK");
            }
        } catch (Exception e) {
            e.printStackTrace();
            rattr.addFlashAttribute("msg", "DEL_ERR");
        }


        return "redirect:/board/list";
    }

    @GetMapping("/read")
    public String read(Integer bno, Integer page, Integer pageSize, Model m){
        try {
            BoardDto boardDto = boardService.read(bno);
            m.addAttribute(boardDto);
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "board";
    }

    @GetMapping("/list")
    public String list(Integer page, Integer pageSize, Model m, HttpServletRequest request) {
        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        if(page==null){
            page =1;
        }
        if(pageSize==null){
            pageSize = 10;
        }

        try {
            int totalCnt = boardService.getCount();
            PageHandler pageHandler = new PageHandler(totalCnt, page, pageSize);

            Map map = new HashMap();
            map.put("offset", (page - 1) * pageSize);
            map.put("pageSize", pageSize);

            List<BoardDto> list = boardService.getPage(map);
            m.addAttribute("list", list);
            m.addAttribute("ph", pageHandler);
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "boardList"; // 로그인을 한 상태이면, 게시판 화면으로 이동
    }

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("id")!=null;
    }
}