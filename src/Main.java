import gui.MyJFrame;

public class Main {
    public static void main(String[] args) {

        //创建学生
        Student s1 = new Student("张三", 2023015801);
        Student s2 = new Student("李四", 2023015802);
        Student s3 = new Student("王五", 2023015803);
        Student s4 = new Student("刘六", 2023015804);
        Student s5 = new Student("钱七", 2023015805);

        //创建小组
        Group G1 = new Group("Group 1", new Student[]{s1, s2});
        Group G2 = new Group("Group 2", new Student[]{s3, s4});
        Group G3 = new Group("Group 3", new Student[]{s5});

        //创建班级
        Classes c1 = new Classes("Class 1", new Group[]{G1, G2, G3}, new Student[]{s1, s2, s3, s4, s5});


        Group randomGroup = c1.selectRandomTeam();
        System.out.println("Randomly selected Group: " + randomGroup.getName());


        Student randomStudentFromGroup = randomGroup.selectRandomStudent();
        System.out.println("Randomly selected student from Group: " + randomStudentFromGroup);


        Student randomStudentFromAll = c1.selectRandomStudentFromAll();
        System.out.println("Randomly selected student from class: " + randomStudentFromAll);

        MyJFrame jfp = new MyJFrame();
        jfp.createPanel();
    }
}