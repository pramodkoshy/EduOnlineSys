export interface ITeacher {
  id?: number;
  name?: string;
  qualifications?: string;
  internalUserId?: number;
}

export class Teacher implements ITeacher {
  constructor(public id?: number, public name?: string, public qualifications?: string, public internalUserId?: number) {}
}
