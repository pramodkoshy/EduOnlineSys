import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { ExamRegistrationDetailComponent } from 'app/entities/exam-registration/exam-registration-detail.component';
import { ExamRegistration } from 'app/shared/model/exam-registration.model';

describe('Component Tests', () => {
  describe('ExamRegistration Management Detail Component', () => {
    let comp: ExamRegistrationDetailComponent;
    let fixture: ComponentFixture<ExamRegistrationDetailComponent>;
    const route = ({ data: of({ examRegistration: new ExamRegistration(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [ExamRegistrationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ExamRegistrationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExamRegistrationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load examRegistration on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.examRegistration).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
