import java.util.Random;

public class Group {
    private String name;
    private Student[] students;

    //初始化小组
    public Group(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    //返回随机抽取的小组名
    public String getName() {
        return name;
    }

    //随机抽取小组成员
    public Student selectRandomStudent() {
        Random random = new Random();
        int index = random.nextInt(students.length);
        return students[index];
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", students=" + java.util.Arrays.toString(students) +
                '}';
    }
}