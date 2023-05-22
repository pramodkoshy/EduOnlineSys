export interface ICourseLearningObjects {
  id?: number;
  name?: string;
  type?: string;
  text?: any;
  url?: string;
  courseUnitName?: string;
  courseUnitId?: number;
}

export class CourseLearningObjects implements ICourseLearningObjects {
  constructor(
    public id?: number,
    public name?: string,
    public type?: string,
    public text?: any,
    public url?: string,
    public courseUnitName?: string,
    public courseUnitId?: number
  ) {}
}
