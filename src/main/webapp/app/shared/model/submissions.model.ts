export const enum Course {
    GSD = 'GSD',
    AI = 'AI',
    WIN = 'WIN'
}

export const enum Subject {
    JAVA = 'JAVA',
    Mlearning = 'Mlearning',
    Middleware = 'Middleware'
}

export const enum Exercise {
    E1 = 'E1',
    E2 = 'E2',
    E3 = 'E3',
    E4 = 'E4',
    E5 = 'E5',
    E6 = 'E6'
}

export interface ISubmissions {
    id?: number;
    fdaiNumber?: string;
    name?: string;
    course?: Course;
    subject?: Subject;
    exercises?: Exercise;
    filesContentType?: string;
    files?: any;
}

export class Submissions implements ISubmissions {
    constructor(
        public id?: number,
        public fdaiNumber?: string,
        public name?: string,
        public course?: Course,
        public subject?: Subject,
        public exercises?: Exercise,
        public filesContentType?: string,
        public files?: any
    ) {}
}
