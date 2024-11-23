import java.util.Random;

public class Classes {
    private String name;
    private Group[] groups;
    private Student[] allStudents;

    //初始化班级中的小组以及所有学生
    public Classes(String name, Group[] groups, Student[] allStudents) {

        this.name = name;
        this.groups = groups;
        this.allStudents = allStudents;
    }

    //随机抽取小组
    public Group selectRandomTeam() {
        Random random = new Random();
        int index = random.nextInt(groups.length);
        return groups[index];
    }

    //从所有人中随机抽取
    public Student selectRandomStudentFromAll() {
        Random random = new Random();
        int index = random.nextInt(allStudents.length);
        return allStudents[index];
    }

    @Override
    public String toString() {
        return "Classes{" +
                "name='" + name + '\'' +
                ", groups=" + java.util.Arrays.toString(groups) +
                ", allStudents=" + java.util.Arrays.toString(allStudents) +
                '}';
    }
}
