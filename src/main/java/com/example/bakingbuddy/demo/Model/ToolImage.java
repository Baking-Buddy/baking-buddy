package com.example.bakingbuddy.demo.Model;

import com.example.bakingbuddy.demo.Model.Tool;

import javax.persistence.*;

@Entity
@Table(name = "tool_images")
public class ToolImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
//    @JoinColumn(name = "tool_id")
    private Tool tool;

    public ToolImage() {}

    public ToolImage(long id, String imageUrl, Tool tool) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.tool = tool;
    }

    public ToolImage(String imageUrl, Tool tool) {
        this.imageUrl = imageUrl;
        this.tool = tool;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }
}
