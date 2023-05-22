import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { CourseUnitDetailComponent } from 'app/entities/course-unit/course-unit-detail.component';
import { CourseUnit } from 'app/shared/model/course-unit.model';

describe('Component Tests', () => {
  describe('CourseUnit Management Detail Component', () => {
    let comp: CourseUnitDetailComponent;
    let fixture: ComponentFixture<CourseUnitDetailComponent>;
    const route = ({ data: of({ courseUnit: new CourseUnit(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [CourseUnitDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CourseUnitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseUnitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courseUnit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseUnit).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
