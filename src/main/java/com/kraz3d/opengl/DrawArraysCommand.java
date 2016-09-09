package com.kraz3d.opengl;

public class DrawArraysCommand {

//    private final Program program;
//
//    private final VertexArray vertexArray;
//
//    private final PrimitiveType primitiveType;
//
//    private final int first;
//
//    private final int count;
//
//    public PrimitiveType getPrimitiveType() {
//        return this.primitiveType;
//    }
//
//    public int getFirst() {
//        return this.first;
//    }
//
//    public int getCount() {
//        return this.count;
//    }
//
//    public static void execute(final DrawArraysCommand command) {
//        execute(Collections.singleton(command));
//    }
//
//    public static void execute(final Collection<DrawArraysCommand> commands) {
//        commands.stream()
//                .collect(Collectors.groupingBy(DrawArraysCommand::getPrimitiveType))
//                .forEach(DrawArraysCommand::multiDraw);
//    }
//
//    private static void multiDraw(final PrimitiveType primitiveType, final Collection<DrawArraysCommand> commands) {
//        final int mode = primitiveType.getGLPrimitiveType();
//        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
//            final IntBuffer firstBuffer = memoryStack.ints(commands.stream().mapToInt(DrawArraysCommand::getFirst).toArray());
//            final IntBuffer countBuffer = memoryStack.ints(commands.stream().mapToInt(DrawArraysCommand::getCount).toArray());
//            GL14.glMultiDrawArrays(mode, firstBuffer, countBuffer);
//        }
//    }

}
